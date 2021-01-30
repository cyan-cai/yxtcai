package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.dao.ServiceMapper;
import com.java.yxt.dao.TerminalCallbackMapper;
import com.java.yxt.service.TerminalCallbackService;
import com.java.yxt.vo.TerminalCallbackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TerminalCallbackServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 回调地址关系表实现类
 * @Package com.java.yxt.service.impl
 * @date 2020/9/17
 */
@Service
public class TerminalCallbackServiceImpl implements TerminalCallbackService {

    @Autowired
    TerminalCallbackMapper terminalCallbackMapper;

    @Autowired
    ServiceMapper serviceMapper;

    /**
     * 插入
     * @param terminalCallbackServiceVo
     * @return
     */
    @Override
    public int insert(TerminalCallbackVo terminalCallbackServiceVo) {
        return terminalCallbackMapper.insert(terminalCallbackServiceVo);
    }

    /**
     * 更新
     * @param terminalCallbackServiceVo
     * @return
     */
    @Override
    @SetCreaterName
    public int update(TerminalCallbackVo terminalCallbackServiceVo) {
        return terminalCallbackMapper.update(terminalCallbackServiceVo);
    }

    @Override
    public int updateByServiceAndTerminalId(TerminalCallbackVo terminalCallbackVo) {
        return terminalCallbackMapper.updateByServiceAndTerminalId(terminalCallbackVo);
    }

    /**
     * 分页查询
     * @param page
     * @param terminalCallbackServiceVo
     * @return
     */
    @Override
    public List<TerminalCallbackVo> select(Page page, TerminalCallbackVo terminalCallbackServiceVo) {
        return terminalCallbackMapper.select(page,terminalCallbackServiceVo);
    }
}
