package com.java.yxt.service;

import com.java.yxt.vo.DictVo;

import java.util.List;

/**
 * 字典表接口
 * @author zanglei
 */
public interface DictService {
    /**
     * 查询
     * @param dictVo
     * @return
     */
    List<DictVo> selectAll(DictVo dictVo);
}
