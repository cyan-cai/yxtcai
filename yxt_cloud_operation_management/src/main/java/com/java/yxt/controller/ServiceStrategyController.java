package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dto.ServiceRateLimit;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.BusinessService;
import com.java.yxt.service.BusinessStrategyService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.vo.ServiceStrategyVo;
import com.java.yxt.vo.ServiceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceStrategyController
 *
 * @author zanglei
 * @version V1.0
 * @description 客户策略
 * @Package com.java.yxt.controller
 * @date 2020/9/17
 */
@RestController
@Slf4j
@Api(tags = {"客户策略"})
@RequestMapping("businessstrategy")
public class ServiceStrategyController {

    @Autowired
    BusinessStrategyService businessStrategyService;

    @Autowired
    BusinessService businessService;

    @Autowired
    RedissonClient redissonUtils;

    @PostMapping("/add")
    @ApiOperation("客户策略新增")
    @SetCreaterName
    @SOLog(tableName = "mgt_service_strategy",remark = "添加客户策略" ,type = OperationType.post)
    public Response addBusinessStrategy(@RequestBody ServiceStrategyVo serviceStrategyVo){

        // 根据service_id查询业务标志code
        ServiceVo serviceVo = new ServiceVo();
        serviceVo.setId(serviceStrategyVo.getServiceId());
        ServiceVo serviceVo1 = businessService.selectById(serviceVo);

        ServiceRateLimit serviceRateLimit = new ServiceRateLimit();
        serviceRateLimit.setServiceCode(serviceVo1.getServiceCode());
        serviceRateLimit.setPerDayFrom(serviceStrategyVo.getDayFrom());
        serviceRateLimit.setPerDayTo(serviceStrategyVo.getDayTo());
        serviceRateLimit.setPerSecondLimit(serviceStrategyVo.getSecondLimit());
        serviceRateLimit.setPerDayLimit(serviceStrategyVo.getDayLimit());
        serviceRateLimit.setPerMonthLimit(serviceStrategyVo.getMonthLimit());
        // 插入客户策略表
        serviceStrategyVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        serviceStrategyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        businessStrategyService.insert(serviceStrategyVo);

        // 同步插入的redis
        RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_SERVICE_LIMIT_KEY);
        map.put(serviceRateLimit.getServiceCode(), JsonUtil.objectToJson(serviceRateLimit));
        return Response.successWithResult(serviceStrategyVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("修改客户策略")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_service_strategy",remark = "更新客户策略",
            mapperName = "serviceStrategyMapper",paramKey = "id")
    public Response updateBusinessStrategy(@RequestBody ServiceStrategyVo serviceStrategyVo){

        // 根据service_id查询业务标志code
        ServiceVo serviceVo = new ServiceVo();
        serviceVo.setId(serviceStrategyVo.getServiceId());
        ServiceVo serviceVo1 = businessService.selectById(serviceVo);

        ServiceRateLimit serviceRateLimit = new ServiceRateLimit();
        serviceRateLimit.setServiceCode(serviceVo1.getServiceCode());
        serviceRateLimit.setPerDayFrom(serviceStrategyVo.getDayFrom());
        serviceRateLimit.setPerDayTo(serviceStrategyVo.getDayTo());
        serviceRateLimit.setPerSecondLimit(serviceStrategyVo.getSecondLimit());
        serviceRateLimit.setPerDayLimit(serviceStrategyVo.getDayLimit());
        serviceRateLimit.setPerMonthLimit(serviceStrategyVo.getMonthLimit());

        int count = businessStrategyService.update(serviceStrategyVo);

        // 同步插入的redis
        RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_SERVICE_LIMIT_KEY);
        map.put(serviceRateLimit.getServiceCode(), JsonUtil.objectToJson(serviceRateLimit));

        return Response.successWithResult(count);
    }

    @PostMapping("/select")
    @ApiOperation("根据条件分页查询")
    public Response selectBusinessStrategy(@RequestBody ServiceStrategyVo serviceStrategyVo){

        Page<ServiceStrategyVo> page = new Page<>(serviceStrategyVo.getCurrent(),serviceStrategyVo.getSize());
        List<OrderItem> orderItemList = new ArrayList<>(1);
        orderItemList.add(OrderItem.desc("mss.create_time"));
        page.setOrders(orderItemList);
        List<ServiceStrategyVo> list = businessStrategyService.select(page,serviceStrategyVo);
        page.setRecords(list);
        return  Response.successWithResult(page);
    }

    @PostMapping("/enabledisable")
    @ApiOperation("启用/禁用")
    @SOLog(type = OperationType.put,tableName = "mgt_service_strategy",remark = "启用禁用客户策略",
            mapperName = "serviceStrategyMapper",paramKey = "id")
    @SetCreaterName
    public Response enableDisable(@RequestBody ServiceStrategyVo serviceStrategyVo){
        int count = businessStrategyService.enableDisable(serviceStrategyVo);
        return  Response.successWithResult(count);
    }

    @PostMapping("/selectall")
    @ApiOperation("不分页查询")
    public Response selectAllBusinessStrategy(@RequestBody ServiceStrategyVo serviceStrategyVo){
        List<ServiceStrategyVo> list = businessStrategyService.selectAll(serviceStrategyVo);
        return  Response.successWithResult(list);
    }
}
