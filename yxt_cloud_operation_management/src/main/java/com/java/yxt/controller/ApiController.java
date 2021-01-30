package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiRelationCatalogService;
import com.java.yxt.service.Apiservice;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ApiVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ApiController
 *
 * @author zanglei
 * @version V1.0
 * @description api管理
 * @Package com.java.yxt.controller
 * @date 2020/9/11
 */
@RestController
@Slf4j
@RequestMapping("api")
@Api(tags = {"api管理"})
public class ApiController {

    @Autowired
    Apiservice apiservice;

    @Autowired
    ApiRelationCatalogService apiRelationCatalogService;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired(required = false)
    DefaultMQProducer rocketMqProducer;


    /**
     * 添加api
     * @param apiVo
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加api")
    @SetCreaterName
    @SOLog(tableName = "mgt_api",remark = "添加api" ,type = OperationType.post)
    public Response addApi(@RequestBody ApiVo apiVo){
        // 插入api表
        int count = apiservice.insert(apiVo);
        return Response.successWithResult(apiVo.getId());
    }

    /**
     * 修改api
     * @param apiVos
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("修改api")
    public Response update(@RequestBody List<ApiVo> apiVos){
        apiVos.stream().forEach(apiVo -> {
            apiservice.update(apiVo);
        });
        return Response.success();
    }

    /**
     * 分页查询
     * @param apiVo
     * @return
     */
    @PostMapping("/select")
    @ApiOperation("分页查询")
    public Response select(@RequestBody ApiVo apiVo){
        Page<ApiVo> page = new Page<>(apiVo.getCurrent(),apiVo.getSize());
        List<OrderItem> orderItemList = Lists.newArrayList();
        orderItemList.add(OrderItem.desc("ma.create_time"));
        page.setOrders(orderItemList);
        List<ApiVo> list = apiservice.select(page,apiVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

    /**
     * 不分页查询
     * @param apiVo
     * @return
     */
    @PostMapping("/selectall")
    @ApiOperation("不分页查询未被管理销售品的api信息")
    public Response selectAllApi(@RequestBody ApiVo apiVo){
        // 如果服务类型是全部则全查
        String str="全部";
        if(ValidateUtil.isNotEmpty(apiVo.getApiCategory()) && str.equals(apiVo.getApiCategory())){
            apiVo.setApiCategory(null);
        }
        List<ApiVo> list = apiservice.selectAllUnRelation(apiVo);
        return Response.successWithResult(list);
    }

    /**
     * 不分页查询
     * @param apiVo
     * @return
     */
    @PostMapping("/selectallapi")
    @ApiOperation("不分页查询所有api信息")
    public Response selectAll(@RequestBody ApiVo apiVo){
        List<ApiVo> list = apiservice.selectAll(apiVo);
        return Response.successWithResult(list);
    }
    /**
     * api删除
     * @param apiVos
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("api删除")
    public Response delete(@RequestBody List<ApiVo> apiVos){
        apiVos.forEach(apiVo -> apiservice.delete(apiVo));
        return Response.success();
    }

}
