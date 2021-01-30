package com.java.yxt.feign;

/**
 * 返回码
 * @author zanglei
 */
public interface IResultCode
{
    /**
     * 获取返回信息
     * @return
     */
    String getMessage();

    /**
     * 获取返回码
     * @return
     */
    int getCode();
}
