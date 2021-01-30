package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dao.ApiMapper;
import com.java.yxt.dao.ApiRelationCatalogMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiRelationCatalogService;
import com.java.yxt.service.Apiservice;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ApiRelationCatalogVo;
import com.java.yxt.vo.ApiVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ApiServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description api管理模块
 * @Package com.java.yxt.service.impl
 * @date 2020/9/14
 */
@Service
@Slf4j
public class ApiServiceImpl implements Apiservice {

    @Autowired
    ApiMapper apiMapper;

    @Autowired
    ApiRelationCatalogService apiRelationCatalogService;

    @Autowired
    ApiRelationCatalogMapper apiRelationCatalogMapper;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired(required = false)
    DefaultMQProducer rocketMqProducer;


    /**
     * 插入
     * @param apiVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ApiVo apiVo) {

        // 判断api名称是否已存在
        ApiVo apiVo2 = new ApiVo();
        apiVo2.setName(apiVo.getName());
        ApiVo apiVo1 = apiMapper.selectByContion(apiVo2);
        if(ValidateUtil.isNotEmpty(apiVo1)){
            log.error("插入api,api名称：{} 已存在",apiVo2.getName());
            throw new MySelfValidateException(ValidationEnum.API_CATALOG_NAME_EXISTS);
        }

        apiVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        int apiInsertCount = apiMapper.insert(apiVo);

        // 有效状态
        if(null!=apiVo.getStatus() && apiVo.getStatus().intValue() ==0  && ValidateUtil.isNotEmpty(apiVo.getExtroPath())){
            // 更新redis
            RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_INFO_KEY);
            map.put(apiVo.getExtroPath(), JsonUtil.objectToJson(apiVo));
        }else if(null!=apiVo.getStatus() && apiVo.getStatus().intValue() ==1 && ValidateUtil.isNotEmpty(apiVo.getExtroPath())){
            // 更新redis,移除key
            RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_INFO_KEY);
            map.remove(apiVo.getExtroPath());
        }
        return  apiInsertCount;
    }

    /**
     * 更新
     * @param apiVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_api",remark = "修改api",mapperName = "apiMapper",paramKey = "id")
    public int update(ApiVo apiVo) {
        //判断是修改还是注销
        if(apiVo.getName()!=null){
            // 判断api名称是否存在
            ApiVo apiVo1 = new ApiVo();
            apiVo1.setName(apiVo.getName());
            ApiVo apiVo2 = apiMapper.selectByContion(apiVo1);
            apiVo1.setId(apiVo.getId());
            //排除修改ID之外的其他API名称重复
            if(ValidateUtil.isNotEmpty(apiVo2)&&(!apiVo1.getId().equals(apiVo2.getId()))){
                    log.error("更新api，名称：{},已存在。",apiVo1.getName());
                    throw  new MySelfValidateException(ValidationEnum.API_CATALOG_NAME_EXISTS);
            }
        }
        // 更新api信息
        int apiInsertCount = apiMapper.update(apiVo);

        // 有效状态
        if(apiVo.getStatus().intValue()==0){
            // 更新redis
            RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_INFO_KEY);
            if(apiVo.getExtroPath() != null){
                map.put(apiVo.getExtroPath(), JsonUtil.objectToJson(apiVo));
            }
        }else{
            // 更新redis,移除key
            RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_INFO_KEY);
            if(apiVo.getExtroPath() != null){
                map.remove(apiVo.getExtroPath());
            }
        }

        return apiInsertCount;
    }

    /**
     * 不分页查询
     * @param apiVo
     * @return
     */
    @Override
    public List<ApiVo> selectAll(ApiVo apiVo) {
        return apiMapper.selectAll(apiVo);
    }

    /**
     * 查询未被销售品管理的api
     * @param apiVo
     * @return
     */
    @Override
    public List<ApiVo> selectAllUnRelation(ApiVo apiVo) {
        return apiMapper.selectAllUnRelation(apiVo);
    }

    /**
     * 分页查询
     * @param apiVo
     * @return
     */
    @Override
    public List<ApiVo> select(Page<?> page, ApiVo apiVo) {
        return apiMapper.select(page,apiVo);
    }

    /**
     * 删除
     * @param apiVo
     * @return
     */
    @Override
    public int delete(ApiVo apiVo) {
        long id=Long.parseLong(apiVo.getId());
        int count = apiMapper.delete(id);
        // 删除对应的API目录关系数据
        ApiRelationCatalogVo apiRelationCatalogVo = new ApiRelationCatalogVo();
        apiRelationCatalogVo.setApiId(apiVo.getId());
        apiRelationCatalogMapper.unRelation(apiRelationCatalogVo);
        return count;
    }

    /**
     * 未下挂api查询
     * @param apiVo
     * @return
     */
    @Override
    public List<ApiVo> unAttachApiSelect(Page page,ApiVo apiVo) {
        return apiMapper.unAttachApiSelect(page,apiVo);
    }

    /**
     * 已下挂api查询
     * @param apiVo
     * @return
     */
    @Override
    public List<ApiVo> attachedApiSelect(Page page,ApiVo apiVo) {
        return apiMapper.attachedApiSelect(page,apiVo);
    }
}
