package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.feign.CustomerFeignService;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiCatalogService;
import com.java.yxt.service.ApiRelationCatalogService;
import com.java.yxt.service.Apiservice;
import com.java.yxt.service.impl.FeignServiceImpl;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ApiCatalogVo;
import com.java.yxt.vo.ApiRelationCatalogVo;
import com.java.yxt.vo.ApiVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiCatalogController
 *
 * @author zanglei
 * @version V1.0
 * @description api目录管理
 * @Package com.java.yxt.controller
 * @date 2020/9/14
 */
@RestController
@Slf4j
@RequestMapping("apicatalog")
@Api(tags = {"api目录管理"})
public class ApiCatalogController {

    @Autowired
    ApiCatalogService apiCatalogService;

    @Autowired
    Apiservice apiservice;

    @Autowired
    CustomerFeignService customerFeignService;

    @Autowired
    FeignServiceImpl feignService;

    @Autowired
    ApiRelationCatalogService apiRelationCatalogService;

    @Autowired
    RedissonClient redissonUtils;
    /**
     * 添加api目录
     * @param apiCatalogVo
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加API目录")
    @SetCreaterName
    @SOLog(tableName = "mgt_api_catalog",remark = "添加api目录" ,type = OperationType.post)
    public Response addApiCatalog(@RequestBody  ApiCatalogVo apiCatalogVo){
        return Response.successWithResult(apiCatalogService.insert(apiCatalogVo));
    }

    /**
     * 修改api目录
     * @param apiCatalogVo
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("修改api目录")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_api_catalog",remark = "修改api目录",mapperName = "apiCatalogMapper",paramKey = "id")
    public Response update(@RequestBody  ApiCatalogVo apiCatalogVo){
        return Response.successWithResult(apiCatalogService.update(apiCatalogVo));
    }

    /**
     * 未下挂api清单查询
     */
    @PostMapping("/unattache")
    @ApiOperation("未下挂api清单查询")
    public Response unAttacheApiSelect(@RequestBody ApiVo apiVo){
        Page<ApiVo> page = new Page<>(apiVo.getCurrent(),apiVo.getSize());
        page.setOptimizeCountSql(false);
        page.addOrder(OrderItem.desc("a.create_time"));
        return Response.successWithResult(page.setRecords(apiservice.unAttachApiSelect(page,apiVo)));
    }

    /**
     * 已下挂api清单查询
     */
    @PostMapping("/attache")
    @ApiOperation("已下挂api清单查询")
    public Response attacheApiSelect(@RequestBody ApiVo apiVo){
        Page<ApiVo> page = new Page<>(apiVo.getCurrent(),apiVo.getSize());
        page.setOptimizeCountSql(false);
        page.addOrder(OrderItem.desc("a.create_time"));
        return Response.successWithResult(page.setRecords(apiservice.attachedApiSelect(page,apiVo)));
    }

    /**
     * api目录查询
     */
    @PostMapping("/selectAll")
    @ApiOperation("api目录分页查询")
    public Response select(@RequestBody ApiCatalogVo apiCatalogVo){
        Page<ApiCatalogVo> page = new Page<>(apiCatalogVo.getCurrent(),apiCatalogVo.getSize());
        List<OrderItem> orderItemList = new ArrayList<>(2);
        orderItemList.add(OrderItem.desc("create_time"));
        page.setOrders(orderItemList);
        List<ApiCatalogVo> list = apiCatalogService.select(page,apiCatalogVo);
        if(ValidateUtil.isEmpty(list)){
            return Response.successWithResult(page.setRecords(list));
        }

        if(ValidateUtil.isNotEmpty(list)){
            list.stream().forEach(a->{
                // 关联的api数量
                ApiVo apiVo =new ApiVo();
                apiVo.setCatalogName(a.getName());
                Integer count = apiCatalogService.selectApiCountByCatalog(apiVo);
                a.setApiCount(count);
            });
        }
        return Response.successWithResult(page.setRecords(list));
    }

    /**
     * api目录查询
     */
    @PostMapping("/select")
    @ApiOperation("api目录不分页查询")
    public Response selectAll(@RequestBody ApiCatalogVo apiCatalogVo){
        List<ApiCatalogVo> list = apiCatalogService.selectCatalogTree(apiCatalogVo);
        return Response.successWithResult(list);
    }

    /**
     * 根据目录查询对应的api
     */
    @PostMapping("/selectapibycatalog")
    @ApiOperation("根据目录查询对应的api")
    public Response selectApiByCatalog(@RequestBody ApiVo apiVo){
        Page<ApiVo> page = new Page<>(apiVo.getCurrent(), apiVo.getSize());
        List<OrderItem> orderItems=new ArrayList<>(2);
        orderItems.add(OrderItem.desc("ma.update_time"));
        orderItems.add(OrderItem.desc("ma.create_time"));
        page.setOrders(orderItems);
        page.setOptimizeCountSql(false);
        List<ApiVo> list = apiCatalogService.selectApiByCatalog(page,apiVo);
        return Response.successWithResult(page.setRecords(list));
    }

    @PostMapping("/relationapi")
    @ApiOperation("目录关联api")
    @SOLog(type = OperationType.post,mapperName = "apiRelationCatalogMapper" ,remark = "API目录下配置挂载API"
    ,tableName = "mgt_api_relation_catalog",paramKey ="id" )
    public Response relationApi(@RequestBody ApiRelationCatalogVo apiRelationCatalogVo){
        int count = apiRelationCatalogService.insert(apiRelationCatalogVo);
        return Response.successWithResult(count);
    }


    @PostMapping("/unrelationapi")
    @ApiOperation("解除目录关联api")
    public Response unRelationApi(@RequestBody List<ApiRelationCatalogVo> apiRelationCatalogVos){
        apiRelationCatalogVos.forEach(apiRelationCatalogVo -> apiRelationCatalogService.unRelation(apiRelationCatalogVo));
        return Response.success();
    }

    @PostMapping("/delete")
    @ApiOperation("api目录删除")
    public Response delete(@RequestBody ApiCatalogVo apiCatalogVo){
        // 判断该目录下是否有子目录，有子目录不允许删除
        ApiCatalogVo apiCatalogVo1 = new ApiCatalogVo();
        apiCatalogVo1.setParentId(apiCatalogVo.getId());
        List<ApiCatalogVo> apiCatalogVos = apiCatalogService.selectAll(apiCatalogVo1);
        if(ValidateUtil.isNotEmpty(apiCatalogVos)){
            log.warn("删除api目录，apiid:{},存在子目录不允许删除。",apiCatalogVo1.getParentId());
            throw new MySelfValidateException(ValidationEnum.APICATALOG_EXISTS_CHILD);
        }
        // 删除api目录,改状态
        apiCatalogService.delete(Long.parseLong(apiCatalogVo.getId()));
        ApiRelationCatalogVo apiRelationCatalogVo = new ApiRelationCatalogVo();
        apiRelationCatalogVo.setApiCatalogId(apiCatalogVo.getId());
        // 改目录和api关系表状态
        int count = apiRelationCatalogService.delete(apiRelationCatalogVo);
        return Response.successWithResult(count);
    }
}
