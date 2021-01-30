package com.java.yxt.exception;

import com.java.yxt.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * ExceptionAdvice
 *
 * @author zanglei
 * @version V1.0
 * @description 全局异常处理
 * @Package com.java.yxt.exception
 * @date 2020/8/28
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * 系统运行异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public Response exception(HttpServletRequest request,Exception e){
        log.error("请求地址：{}，服务器异常",request.getRequestURI(),e);
        return new Response(500,"服务器异常！",e.getMessage());
    }

    /**
     * 自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(MySelfValidateException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public Response shortDataValidateException (MySelfValidateException e ){
        log.error("业务异常:code={},message={}",e.getCode(),e.getValue());
        return new Response(e.getCode(),e.getValue().toString());
    }

}
