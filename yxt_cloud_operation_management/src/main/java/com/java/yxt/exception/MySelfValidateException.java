package com.java.yxt.exception;

import com.java.yxt.base.IStatusEnum;
import lombok.Data;

/**
 * ShortDataValidateException
 *
 * @author zanglei
 * @version V1.0
 * @description 自定义异常
 * @Package com.java.yxt.exception
 * @date 2020/8/28
 */
@Data
public class MySelfValidateException extends RuntimeException {

    private Integer code;

    private Object value;

    public MySelfValidateException (IStatusEnum statusEnum){
//        super( statusEnum.getMessage());
        this.value = statusEnum.getMessage();
        code = statusEnum.getCode();
    }

    public MySelfValidateException (Integer code,Object value){
        this.code = code;
        this.value = value;
    }
}
