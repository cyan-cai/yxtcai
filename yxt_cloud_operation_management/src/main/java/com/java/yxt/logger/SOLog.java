package com.java.yxt.logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SOLog {
     //操作表名
     String tableName() default  "";
     //操作类型
     OperationType type() default OperationType.notDef;
     //备注
     String remark()default "";
     //操作数据的主键id，用来查询修改前的旧值
     String paramKey() default "";
     /**
      * 操作表对应的mapper（首字母小写），现在已经根据表名自动转换成mapper，(要求mapper类名组成为表名驼峰命名+Mapper)
      * eg：  表名: nc_department -----> mapper: ncDepartmentMapper
      */
     String mapperName() default "";
}
