package com.java.yxt.controller;

import com.java.yxt.base.Response;
import com.java.yxt.service.DictService;
import com.java.yxt.vo.DictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DictionaryController
 *
 * @author zanglei
 * @version V1.0
 * @description 字典表管理
 * @Package com.java.yxt.controller
 * @date 2020/9/23
 */
@RestController
@Slf4j
@RequestMapping("dict")
@Api(tags = {"字典表处理"})
public class DictionaryController {
    @Autowired
    DictService dictService;

    @PostMapping("/select")
    @ApiOperation("字典表查询")
    public Response selectDict(@RequestBody DictVo dictVo){

        return Response.successWithResult(dictService.selectAll(dictVo));
    }
}
