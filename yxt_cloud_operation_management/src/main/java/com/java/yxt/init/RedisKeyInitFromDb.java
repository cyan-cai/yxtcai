package com.java.yxt.init;

import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dao.CustomerMapper;
import com.java.yxt.dao.TerminalWhiteMapper;
import com.java.yxt.dto.CustomerInfo;
import com.java.yxt.dto.ServiceCodeMsisdnsDto;
import com.java.yxt.service.BusinessService;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.CustomerVo;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalWhiteVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.init
 * @date 2020/11/23
 */
@Slf4j
@Component
public class RedisKeyInitFromDb implements CommandLineRunner {

    @Autowired
    BusinessService businessService;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    TerminalWhiteMapper terminalWhiteMapper;

    @Override
    public void run(String... args) throws Exception {

        /**
         * 同步redis,客户对应的业务标识信息
         */
        //查询所有客户
        CustomerVo customerVo = new CustomerVo();
        List<CustomerVo> customerVos = customerMapper.selectAll(customerVo);

        if(ValidateUtil.isEmpty(customerVos)){
            log.warn("客户信息为空，不做redis缓存初始化。");
        }else {
            for (CustomerVo customer : customerVos) {
                // 查询客户下面状态是有效的业务标识
                ServiceVo serviceVo1 = new ServiceVo();
                serviceVo1.setStatus(1);
                serviceVo1.setCustomerCode(customer.getCustomerCode());
                List<ServiceVo> serviceVoList = businessService.selectAll(serviceVo1);
                if (ValidateUtil.isEmpty(serviceVoList)) {
                    continue;
                }
                // 获取servicecodes列表
                List<String> serviceCodes = serviceVoList.stream().map(serviceVo2 -> serviceVo2.getServiceCode()).collect(Collectors.toList());
                // 客户信息对象初始化
                CustomerInfo customerInfo = new CustomerInfo();
                customerInfo.setServiceCode(serviceCodes);
                customerInfo.setCustomerCode(serviceVo1.getCustomerCode());
                customerInfo.setStatus(customer.getStatus());
                // 存入redis
                RBucket<String> customerServiceCode = redissonUtils.getBucket(RedisKeyConst.GATEWAY_CUSTOMER_INFO_DIR + serviceVo1.getCustomerCode());
                customerServiceCode.set(JsonUtil.objectToJson(customerInfo));
            }
        }

        /**
         * 同步redis业务标识对应的终端信息
         */
        // 查询业务标识信息
        ServiceVo serviceVo1 = new ServiceVo();
        serviceVo1.setStatus(1);
        List<ServiceVo> serviceVoList = businessService.selectAll(serviceVo1);
        if(ValidateUtil.isNotEmpty(serviceVoList)){
            serviceVoList.stream().forEach(serviceVo->{
                TerminalWhiteVo terminalWhiteVo = new TerminalWhiteVo();
                terminalWhiteVo.setServiceCode(serviceVo.getServiceCode());
                List<TerminalWhiteVo> terminalWhiteVos = terminalWhiteMapper.terminalWhiteInfo(terminalWhiteVo);
                List<String> msisdnList= terminalWhiteVos.stream().map(a->a.getMsisdn()).collect(Collectors.toList());

                ServiceCodeMsisdnsDto serviceCodeMsisdnsDto = new ServiceCodeMsisdnsDto();
                serviceCodeMsisdnsDto.setServiceCode(serviceVo.getServiceCode());
                serviceCodeMsisdnsDto.setStartTime(serviceVo.getStartTime());
                serviceCodeMsisdnsDto.setEndTime(serviceVo.getEndTime());
                serviceCodeMsisdnsDto.setMsisdnList(msisdnList);

                RBucket<String> bucket = redissonUtils.getBucket(RedisKeyConst.GATEWAY_SERVICE_WHILTE_DIR+serviceVo.getServiceCode());
                bucket.set(JsonUtil.objectToJsonSetDateFormat(serviceCodeMsisdnsDto));
            });
        }
    }
}
