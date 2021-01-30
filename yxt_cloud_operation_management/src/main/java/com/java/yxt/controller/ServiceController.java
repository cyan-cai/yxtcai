package com.java.yxt.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.service.BusinessService;
import com.java.yxt.service.ProductionService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ProductionVo;
import com.java.yxt.vo.ServiceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ServiceController
 *
 * @author zanglei
 * @version V1.0
 * @description 业务标识管理
 * @Package com.java.yxt.controller
 * @date 2020/9/17
 */
@RestController
@Slf4j
@Api(tags = {"业务标识管理"})
@RequestMapping("business")
public class ServiceController {

    @Autowired
    BusinessService businessService;
    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    ProductionService productionService;

    @PostMapping("/add")
    @ApiOperation("添加业务标识信息")
    @Transactional(rollbackFor = Exception.class)
    @SOLog(tableName = "mgt_service",remark = "添加业务标识记录" ,type = OperationType.post)
    @SetCreaterName
    public Response insertBusiness(@RequestBody ServiceVo serviceVo){
        businessService.insert(serviceVo);
        return Response.successWithResult(serviceVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("更新业务标识信息")
    @SOLog(type = OperationType.put,tableName = "mgt_service",remark = "更新回调URL地址",mapperName = "serviceMapper",paramKey = "id")
    public Response updateBusiness(@RequestBody ServiceVo serviceVo)
    {
        businessService.update(serviceVo);
        return Response.success();
    }

    @PostMapping("/select")
    @ApiOperation("回调管理分页查询")
    public Response selectBusiness(@RequestBody ServiceVo serviceVo)
    {
        Page<ServiceVo> page =new Page<>(serviceVo.getCurrent(),serviceVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(1);
        orderItems.add(OrderItem.desc("ms.create_time"));
        page.setOrders(orderItems);
        List<ServiceVo> list =   businessService.select(page,serviceVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

    @PostMapping("/selectsubscribe")
    @ApiOperation("订购关系分页查询")
    public Response selectSubscribe(@RequestBody ServiceVo serviceVo)
    {
        Page<ServiceVo> page =new Page<>(serviceVo.getCurrent(),serviceVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(1);
        orderItems.add(OrderItem.desc("ms.create_time"));
        page.setOrders(orderItems);
        page.setOptimizeCountSql(false);
        List<ServiceVo> list =   businessService.selectSubscribe(page,serviceVo);
        list.stream().forEach(serviceVo1 -> {
            Date endTime = serviceVo1.getEndTime();
            Date startTime = serviceVo1.getStartTime();
            Date now = DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now());
            // 开始日期和当前日期比较
            long beginBtwnSeconds = DateUtil.between(now, startTime, DateUnit.SECOND, false);
            // 到期日和当期日期比较
            long endBtwnSeconds = DateUtil.between(now, endTime, DateUnit.SECOND, false);
            // 如果到期日小于等于当前时间，则为失效状态
            if (beginBtwnSeconds < 0 && endBtwnSeconds > 0) {
                serviceVo1.setStatus(1);
            } else {
                serviceVo1.setStatus(0);
            }
        });

        page.setRecords(list);
        return Response.successWithResult(page);
    }


    @PostMapping("/selectall")
    @ApiOperation("不分页查询")
    public Response selectAllBusiness(@RequestBody ServiceVo serviceVo)
    {
        List<ServiceVo> list =   businessService.selectAll(serviceVo);
        return Response.successWithResult(list);
    }


    @PostMapping("/detail")
    @ApiOperation("业务标识详情")
    public Response selectBusinessDetail(@RequestBody ServiceVo serviceVo)
    {
        List<ServiceVo> list =   businessService.selectAll(serviceVo);
        if(ValidateUtil.isNotEmpty(list)){
            ServiceVo serviceVo1 = list.get(0);
            // 获取销售品编码
            String productId = serviceVo1.getProductId();
            ProductionVo productionVo = new ProductionVo();
            productionVo.setSaleCode(productId);
            // 根据销售品编码查询销售品信息
           List<ProductionPo> list1 = productionService.selectAllProduciton(productionVo);
            serviceVo1.setProductionPos(list1);
        }
        return Response.successWithResult(list);
    }

}
