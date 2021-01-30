package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.IpWhiteService;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.IpWhiteVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * IpWhiteController
 *
 * @author zanglei
 * @version V1.0
 * @description ip白名单管理
 * @Package com.java.yxt.controller
 * @date 2020/9/24
 */
@RestController
@RequestMapping("ipwhite")
@Api(tags = {"ip白名单管理"})
@Slf4j
public class IpWhiteController {
    @Autowired
    IpWhiteService ipWhiteService;

    @Autowired
    RedissonClient redissonUtils;

    @PostMapping("/add")
    @ApiOperation("插入")
    @SetCreaterName
    @SOLog(tableName = "mgt_ip_white",remark = "IP白名单添加" ,type = OperationType.post)
    public Response addIpWhite(@RequestBody IpWhiteVo ipWhiteVo){
        ipWhiteService.insert(ipWhiteVo);
        return Response.successWithResult(ipWhiteVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("更新")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_ip_white",remark = "更新IP白名单",mapperName = "ipWhiteMapper",paramKey = "id")
    public Response updateIpWhite(@RequestBody IpWhiteVo ipWhiteVo){
        ipWhiteService.update(ipWhiteVo);
        return Response.success();
    }


    @PostMapping("/select")
    @ApiOperation("分页查询")
    public Response selectIpWhite(@RequestBody IpWhiteVo ipWhiteVo){
        Page<IpWhiteVo> page = new Page<>(ipWhiteVo.getCurrent(),ipWhiteVo.getSize());
        if(ValidateUtil.isNotEmpty(ipWhiteVo.getCreateTimeList())){
            Object [] createTime=ipWhiteVo.getCreateTimeList().toArray();
            ipWhiteVo.setCreateBeginTime((String)createTime[0]);
            ipWhiteVo.setCreateEndTime((String)createTime[1]);
        }
        List<OrderItem> orderItemList= Lists.newArrayList();
        orderItemList.add(OrderItem.desc("miw.create_time"));
        page.setOrders(orderItemList);
        List<IpWhiteVo> list=ipWhiteService.select(page,ipWhiteVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

    @PostMapping("/selectall")
    @ApiOperation("不分页查询")
    public Response selectAllIpWhite(@RequestBody IpWhiteVo ipWhiteVo){
        List<IpWhiteVo> list = ipWhiteService.selectAll(ipWhiteVo);
        return Response.successWithResult(list);
    }

    @GetMapping("/export")
    @ApiOperation("批量导出")
    @SOLog(tableName = "mgt_ip_white",remark = "IP白名单导出" )
    public Response export(IpWhiteVo ipWhiteVo, HttpServletResponse response){
        ipWhiteService.export(ipWhiteVo,response);
        return Response.success();
    }

}
