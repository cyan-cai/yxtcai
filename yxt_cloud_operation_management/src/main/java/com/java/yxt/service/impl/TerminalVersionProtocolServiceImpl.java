package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.dao.TerminalVersionProtocolMapper;
import com.java.yxt.service.TerminalVersionProtocolService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.TerminalVersionProtocolVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zanglei
 * @version V1.0
 * @description 终端版本协议管理
 * @Package com.java.yxt.service.impl
 * @date 2021/1/18
 */
@Service
@Slf4j
public class TerminalVersionProtocolServiceImpl implements TerminalVersionProtocolService {
    @Autowired
    TerminalVersionProtocolMapper terminalVersionProtocolMapper;

    @Override
    public int addRecord(TerminalVersionProtocolVo terminalVersionProtocolVo) {
        terminalVersionProtocolVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalVersionProtocolMapper.insert(terminalVersionProtocolVo);
    }

    @Override
    public int update(TerminalVersionProtocolVo terminalVersionProtocolVo) {
        terminalVersionProtocolVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalVersionProtocolMapper.updateById(terminalVersionProtocolVo);
    }

    @Override
    public int delete(TerminalVersionProtocolVo terminalVersionProtocolVo) {
       return terminalVersionProtocolMapper.updateById(terminalVersionProtocolVo);
    }

    @Override
    public IPage<TerminalVersionProtocolVo> selectByPage(Page page, TerminalVersionProtocolVo terminalVersionProtocolVo) {
        QueryWrapper<TerminalVersionProtocolVo> queryWrapper= new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.like(ValidateUtil.isNotEmpty(terminalVersionProtocolVo.getTerminalVersion()),"terminal_version",terminalVersionProtocolVo.getTerminalVersion());
        queryWrapper.eq(ValidateUtil.isNotEmpty(terminalVersionProtocolVo.getTerminalType()),"terminal_type",terminalVersionProtocolVo.getTerminalType());
        queryWrapper.eq(ValidateUtil.isNotEmpty(terminalVersionProtocolVo.getTerminalFactory()),"terminal_facotory",terminalVersionProtocolVo.getTerminalFactory());
        queryWrapper.ne("status",0);
        return terminalVersionProtocolMapper.selectPage(page,queryWrapper);
    }

    @Override
    public List<TerminalVersionProtocolVo> selectAll(TerminalVersionProtocolVo terminalVersionProtocolVo) {
        QueryWrapper<TerminalVersionProtocolVo> queryWrapper = new QueryWrapper<>();
        return terminalVersionProtocolMapper.selectList(queryWrapper);
    }
}
