package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dto.RouteRateLimit;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiStrategyService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ApiStrategyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ApiStrategyController
 *
 * @author zanglei
 * @version V1.0
 * @description api策略管理
 * @Package com.java.yxt.controller
 * @date 2020/9/14
 */
@RestController
@Slf4j
@Api(tags = {"API策略管理"})
@RequestMapping("apistrategy")
public class ApiStrategyController {

    @Autowired
    ApiStrategyService apiStrategyService;

    @Autowired
    RedissonClient redissonUtils;

    @PostMapping("/add")
    @ApiOperation("api策略添加")
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public Response addStrategy(@RequestBody  ApiStrategyVo apiStrategyVo){

        // 插入策略表
        apiStrategyVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        long count = apiStrategyService.insert(apiStrategyVo);

        RouteRateLimit routeRateLimit = new RouteRateLimit();
        routeRateLimit.setExtroPath(apiStrategyVo.getExtroPath());
        routeRateLimit.setInternalPath(apiStrategyVo.getInternalPath());
        routeRateLimit.setPerSecondLimit(apiStrategyVo.getSecondLimit());
        routeRateLimit.setPerDayLimit(apiStrategyVo.getDayLimit());
        routeRateLimit.setPerMonthLimit(apiStrategyVo.getMonthLimit());
        routeRateLimit.setPerDayFrom(apiStrategyVo.getDayFrom());
        routeRateLimit.setPerDayTo(apiStrategyVo.getDayTo());

        if(ValidateUtil.isEmpty(apiStrategyVo.getExtroPath())){
            return Response.successWithResult(apiStrategyVo.getId());
        }

        if(null!=apiStrategyVo.getStatus() && apiStrategyVo.getStatus().intValue()==1){
            // 同步插入redis
            RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_LIMIT_KEY);
            String string = null;
            string = JsonUtil.objectToJson(routeRateLimit);
            map.put(apiStrategyVo.getExtroPath(),string);
        }else if (null!=apiStrategyVo.getStatus() && apiStrategyVo.getStatus().intValue()==0){
            // 同步插入redis,移除key
            RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_LIMIT_KEY);
            map.remove(apiStrategyVo.getExtroPath());
        }

        return Response.successWithResult(apiStrategyVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("api更新")
    @Transactional(rollbackFor = Exception.class)
    @SOLog(type = OperationType.put,tableName = "mgt_api_strategy",remark = "修改api策略",mapperName = "apiStrategyMapper",paramKey = "id")
    public Response updateStrategy(@RequestBody  ApiStrategyVo apiStrategyVo){

        apiStrategyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        int count = apiStrategyService.update(apiStrategyVo);

        RouteRateLimit routeRateLimit = new RouteRateLimit();
        routeRateLimit.setExtroPath(apiStrategyVo.getExtroPath());
        routeRateLimit.setInternalPath(apiStrategyVo.getInternalPath());
        routeRateLimit.setPerSecondLimit(apiStrategyVo.getSecondLimit());
        routeRateLimit.setPerDayLimit(apiStrategyVo.getDayLimit());
        routeRateLimit.setPerMonthLimit(apiStrategyVo.getMonthLimit());
        routeRateLimit.setPerDayFrom(apiStrategyVo.getDayFrom());
        routeRateLimit.setPerDayTo(apiStrategyVo.getDayTo());

        if(ValidateUtil.isEmpty(apiStrategyVo.getExtroPath())){
            return Response.successWithResult(apiStrategyVo.getId());
        }

        RMap<String, String> map = redissonUtils.getMap(RedisKeyConst.GATEWAY_ROUTE_LIMIT_KEY);
        if(apiStrategyVo.getStatus().intValue()==1){
            // 同步插入redis
            String string = null;
            string = JsonUtil.objectToJson(routeRateLimit);
            map.put(apiStrategyVo.getExtroPath(),string);
        }else{
            // 同步插入redis,移除key
            map.remove(apiStrategyVo.getExtroPath());
        }
        return Response.success();
    }

    @PostMapping("/select")
    @ApiOperation("分页策略查询")
    public Response selectStrategy(@RequestBody ApiStrategyVo apiStrategyVo){
        Page<ApiStrategyVo> page = new Page(apiStrategyVo.getCurrent(),apiStrategyVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(2);
        orderItems.add(OrderItem.desc("ma.update_time"));
        orderItems.add(OrderItem.desc("ma.create_time"));
        page.setOrders(orderItems);
        List<ApiStrategyVo> list = apiStrategyService.select(page,apiStrategyVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }
}
