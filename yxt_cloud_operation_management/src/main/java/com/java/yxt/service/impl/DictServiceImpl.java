package com.java.yxt.service.impl;

import com.java.yxt.dao.DictMapper;
import com.java.yxt.service.DictService;
import com.java.yxt.vo.DictVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DictServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.service.impl
 * @date 2020/9/23
 */
@Service
@Slf4j
public class DictServiceImpl implements DictService {
    @Autowired
    DictMapper dictMapper;

    /**
     * 查询字典表
     * @param dictVo
     * @return
     */
    @Override
    public List<DictVo> selectAll(DictVo dictVo) {
        return dictMapper.selectAll(dictVo);
    }
}
