package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.service.ServeridService;
import com.java.yxt.vo.ServeridVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Serverid Controller
 * </p>
 *
 * @author zanglei
 * @date 2020-09-25
 */
@Slf4j
@RestController
@RequestMapping("serverid")
public class ServeridController {

    /**
     * <p>
     * ServeridService
     * </p>
     */
    @Autowired
    private ServeridService serveridService;

    /**
     * <p>
     * Get Serverid by id
     * </p>
     * @param serveridVo
     * @return Response
     */
    @GetMapping("/select")
    public Response getById(@RequestBody ServeridVo serveridVo) {
        Page<ServeridVo> page =new Page<>(serveridVo.getCurrent(),serveridVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.desc("update_time"));
        page.setOrders(orderItems);
        List<ServeridVo> list = serveridService.select(page,serveridVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

    /**
     * <p>
     * Add Serverid
     * </p>
     * @param serveridVo
     */
    @PostMapping("/add")
    public Response add(@RequestBody ServeridVo serveridVo) {
      return  Response.successWithResult(serveridService.save(serveridVo));
    }

    /**
     * <p>
     * Update Serverid
     * </p>
     * @param serveridVo
     */
    @PutMapping("/update")
    public Response update(@RequestBody ServeridVo serveridVo) {
        return Response.successWithResult(serveridService.updateById(serveridVo));
    }

    /**
     * <p>
     * Delete Serverid by id
     * </p>
     * @param serveridVo
     */
    @DeleteMapping("/delete")
    public Response delete(@RequestBody ServeridVo serveridVo) {
      return Response.successWithResult(serveridService.removeById(serveridVo))  ;
    }

}
