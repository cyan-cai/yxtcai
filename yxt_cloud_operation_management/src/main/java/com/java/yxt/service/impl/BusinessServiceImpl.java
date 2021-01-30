package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dao.ApiMapper;
import com.java.yxt.dao.CustomerMapper;
import com.java.yxt.dao.ServiceMapper;
import com.java.yxt.dto.CustomerInfo;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.service.BusinessService;
import com.java.yxt.service.CustomerService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BusinessServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 业务标识处理实现类
 * @Package com.java.yxt.service.impl
 * @date 2020/9/17
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    ApiMapper apiMapper;

    @Autowired
    CustomerService customerService;
    /**
     * 插入业务标识信息
     * @param serviceVo
     * @return
     */
    @Override
    public int insert(ServiceVo serviceVo) {
        //查看客户是否已订购过此商品
        List<ServiceVo> list=serviceMapper.selectByCustomerandProduct(serviceVo);
        if(ValidateUtil.isNotEmpty(list)){
            log.warn("添加业务标识出错，当前客户已订购过该商品");
            throw new MySelfValidateException(ValidationEnum.SERVICE_CUSTOMERPRODUCT_EXISTS);
        }
        // 检查业务标识是否存在
        ServiceVo serviceVo3 = new ServiceVo();
        serviceVo3.setServiceCode(serviceVo.getServiceCode());
        List<ServiceVo> serviceVos = serviceMapper.selectAll(serviceVo3);
        if(ValidateUtil.isNotEmpty(serviceVos)){
            log.warn("添加业务标识出错，业务标识：{}，已存在",serviceVo3.getServiceCode());
            throw new MySelfValidateException(ValidationEnum.SERVICE_SERVICECODE_EXISTS);
        }
        //查看商品关联API是否包含短报文服务，包含则开通SaaS及短报文服务
        List<ApiVo> apiVos=apiMapper.selectBySaleCode(serviceVo.getProductId());
        String serviceTypeStr="";
        for (ApiVo apiVo:apiVos) {
            serviceTypeStr+=apiVo.getApiCategory();
        }
        serviceVo.setServiceType(serviceTypeStr);
//        serviceVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        int count =  serviceMapper.insert(serviceVo);

        // 根据客户编码查询对应的业务标识信息
        ServiceVo serviceVo1 = new ServiceVo();
        serviceVo1.setCustomerCode(serviceVo.getCustomerCode());
        serviceVo1.setStatus(1);
        List<ServiceVo> serviceVoList = selectAll(serviceVo1);

        if(ValidateUtil.isNotEmpty(serviceVoList)){
            // 获取servicecodes列表
            List<String> serviceCodes = serviceVoList.stream().map(serviceVo2->serviceVo2.getServiceCode()).collect(Collectors.toList());

            CustomerVo customerVo = new CustomerVo();
            customerVo.setCustomerCode(serviceVo1.getCustomerCode());

            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setServiceCode(serviceCodes);
            customerInfo.setCustomerCode(serviceVo1.getCustomerCode());
            // 查询客户信息
            List<CustomerVo> customerVos = customerMapper.selectAll(customerVo);
            if(ValidateUtil.isNotEmpty(customerVos)){
                customerInfo.setStatus(customerVos.get(0).getStatus());
            }
            // 把客户下面对应的业务标识同步到redis
            RBucket<String> customerServiceCode  =  redissonUtils.getBucket(RedisKeyConst.GATEWAY_CUSTOMER_INFO_DIR+serviceVo1.getCustomerCode());
            customerServiceCode.set(JsonUtil.objectToJson(customerInfo));
        }
        return count;
    }

    /**
     * 分页查询
     * @param page
     * @param serviceVo
     * @return
     */
    @Override
    public List<ServiceVo> select(Page<?> page, ServiceVo serviceVo) {
        return serviceMapper.select(page,serviceVo);
    }

    /**
     * 订阅关系查询
     * @param page
     * @param serviceVo
     * @return
     */
    @Override
    public List<ServiceVo> selectSubscribe(Page<?> page, ServiceVo serviceVo) {
        return serviceMapper.selectSubscribe(page,serviceVo);
    }

    /**
     * 不分页查询
     * @param serviceVo
     * @return
     */
    @Override
    public List<ServiceVo> selectAll(ServiceVo serviceVo) {
        return serviceMapper.selectAll(serviceVo);
    }

    /**
     * 客户白名单没有被关联的业务标识
     * @param terminalWhiteVo
     * @return
     */
    @Override
    public List<ServiceVo> unRelationWhiteServiceCode(TerminalWhiteVo terminalWhiteVo) {
        return serviceMapper.unRelationWhiteServiceCode(terminalWhiteVo);
    }

    /**
     * sp客户未被关联的黑名单的业务标识
     * @param terminalBlackVo
     * @return
     */
    @Override
    public List<ServiceVo> unRelationBlackServiceCode(TerminalBlackVo terminalBlackVo) {
        return serviceMapper.unRelationBlackServiceCode(terminalBlackVo);
    }

    /**
     * 查询有效业务标识信息
     * @param serviceVo
     * @return
     */
    @Override
    public List<ServiceVo> selectEnableBusiness(ServiceVo serviceVo) {
        return serviceMapper.selectEnableBusinewss(serviceVo);
    }

    /**
     * 更新
     * @param serviceVo
     * @return
     */
    @Override
    public int update(ServiceVo serviceVo) {
        serviceVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return serviceMapper.update(serviceVo);
    }

    /**
     * 单条查询
     * @param serviceVo
     * @return
     */
    @Override
    public ServiceVo selectById(ServiceVo serviceVo) {
        return serviceMapper.selectById(serviceVo);
    }

    /**
     * 根据客户id查询业务标识
     * @param serviceVo
     * @return
     */
    @Override
    public List<ServiceVo> selectByCustomerId(ServiceVo serviceVo) {
        return serviceMapper.selectAll(serviceVo);
    }

    /**
     * 业务标识到期状态修改
     * @param serviceCodes
     * @return
     */
    @Override
    public int updateDisableStatus(List<String> serviceCodes) {
        return 0;
    }

}
