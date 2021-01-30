package com.java.yxt.dao;

import com.java.yxt.vo.DictVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典表接口
 * @author  zanglei
 */
@Mapper
public interface DictMapper {
    /**
     * 查询
     * @param dictVo
     * @return List<DictVo>
     */
    List<DictVo> selectAll(DictVo dictVo);

}