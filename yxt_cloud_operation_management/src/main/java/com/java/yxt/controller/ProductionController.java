package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.service.ProductionApiRelationService;
import com.java.yxt.service.ProductionService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.ProductionApiRelationVo;
import com.java.yxt.vo.ProductionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ProductController
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品控制层
 * @Package com.java.yxt.controller
 * @date 2020/9/9
 */
@RestController
@Slf4j
@RequestMapping("production")
@Api( tags = { "销售品管理" } )
public class ProductionController {

    @Autowired
    ProductionService productionService;

    @Autowired
    ProductionApiRelationService productionApiRelationService;


    @PostMapping("/add")
    @ApiOperation("添加销售品")
    @SetCreaterName
    @SOLog(tableName = "mgt_production",remark = "添加销售品" ,type = OperationType.post)
    public Response addProduction(@RequestBody ProductionVo productionVo){
        productionService.addProduction(productionVo);
        return Response.successWithResult(productionVo);
    }

    @PostMapping("/update")
    @ApiOperation("更新销售品")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_production",remark = "更新销售品信息",mapperName = "productionMapper",paramKey = "id")
    public Response updateProduction(@RequestBody ProductionVo productionVo){
        return Response.successWithResult(productionService.updateProduction(productionVo));
    }

    @PostMapping("/addrelationapi")
    @ApiOperation("添加销售品api关联")
    @SOLog(tableName = "mgt_production_api_relation",remark = "添加销售品api关联" ,type = OperationType.post)
    public Response addProductionApiRelation(@RequestBody ProductionApiRelationVo productionApiRelationVo){
        // 创建时间
        productionApiRelationVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        // 雪花算法生成id
//        productionApiRelationVo.setId(String.valueOf(SequenceWorker.randomSequence()));
       return Response.successWithResult(productionApiRelationService.add(productionApiRelationVo));
    }

    @PostMapping("/addProductionAndRelationApi")
    @ApiOperation("新增销售品然后api关联")
    @SOLog(tableName = "mgt_production_api_relation",remark = "新增销售品然后api关联" ,type = OperationType.post)
    public Response addProductionAndApiRelation(@RequestBody ProductionApiRelationVo productionApiRelationVo){
        //新增销售品
        ProductionVo productionVo=new ProductionVo();
        productionVo.setSaleCode(productionApiRelationVo.getSaleCode());
        productionVo.setName(productionApiRelationVo.getProductName());
        productionVo.setChannel(productionApiRelationVo.getChannel());
        productionVo.setStatus(1);
        productionService.addProduction(productionVo);
        //添加销售品关联
        productionApiRelationVo.setProductId(productionVo.getId());
        productionApiRelationVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return Response.successWithResult(productionApiRelationService.add(productionApiRelationVo));
    }

    @PostMapping("/selectrelationapi")
    @ApiOperation("销售品api关联查询")
    public Response selectProductionApiRelation(@RequestBody ProductionApiRelationVo productionApiRelationVo){
        return Response.successWithResult(productionApiRelationService.select(productionApiRelationVo));
    }

    @PostMapping("/select")
    @ApiOperation("销售品分页查询")
    public Response<Page<ProductionPo>> selectProduction(@RequestBody ProductionVo productionVo){
        Page<ProductionPo> page = new Page<>(productionVo.getCurrent(),productionVo.getSize());
        List<OrderItem> orderItems= Lists.newArrayList();
        orderItems.add(OrderItem.desc("create_time"));
        page.setOrders(orderItems);
        List<ProductionPo> list = productionService.selectProduciton(page,productionVo);
        page.setRecords(list);
        return  Response.successWithResult(page);
    }

    @PostMapping("/selectall")
    @ApiOperation("销售品查询")
    public Response selectAllProduction(@RequestBody ProductionVo productionVo){
        List<ProductionPo>  list= productionService.selectAllProduciton(productionVo);
        return  Response.successWithResult(list);
    }

    @PostMapping("/selectallValid")
    @ApiOperation("查询所有有效销售品")
    public Response selectAllValidProduction(@RequestBody ProductionVo productionVo){
        productionVo.setStatus(1);
        List<ProductionPo>  list= productionService.selectAllValid(productionVo);
        return  Response.successWithResult(list);
    }

    @PostMapping("/checkProduction")
    @ApiOperation("销售品重复性校验")
    public Response checkProduction(@RequestBody ProductionVo productionVo){
        productionService.checkProduction(productionVo);
        return  Response.success();
    }

    @PostMapping("/checkProductionIsOrdered")
    @ApiOperation("销售品是否被订购校验")
    public Response checkProductionIsOrdered(@RequestBody ProductionVo productionVo){
        Response response=productionService.checkProductionIsOrdered(productionVo);
        return  response;
    }
}
