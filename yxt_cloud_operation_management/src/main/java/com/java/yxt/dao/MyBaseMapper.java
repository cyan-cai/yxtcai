package com.java.yxt.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MyBaseMapper<T>
{
    @Select("select * from ${tableName} where id=#{id}")
    public T selectById(@Param("tableName")String tableName, @Param("id")String id);

}
