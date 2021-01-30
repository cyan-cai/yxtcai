package com.java.yxt.dao;

import com.java.yxt.po.UpLoadErrorPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * api dao 层接口
 * @author zanglei
 */
@Mapper
public interface UploadErrorMapper {


    /**
     * 插入
     * @param upLoadErrorPo
     * @return
     */
    int insert(UpLoadErrorPo upLoadErrorPo);


    /**
     * 根据key查询keyText
     * @param key
     * @return
     */
    UpLoadErrorPo selectByKey(String key);

    /**
     * 清空表
     * @return
     */
    int delete();
}