package com.java.yxt.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.feign
 * @date 2020/10/15
 */
@ApiModel(
        description = "返回信息"
)
@Data
public class ResultObject<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(
            value = "状态码",
            required = true
    )
    private int code;
    @ApiModelProperty(
            value = "是否成功",
            required = true
    )
    private boolean success;
    @ApiModelProperty("承载数据")
    private T data;
    @ApiModelProperty(
            value = "返回消息",
            required = true
    )
    private String msg;

    private ResultObject(int code, boolean success, T data, String msg) {
        this.code = code;
        this.success = success;
        this.data = data;
        this.msg = msg;
    }


    private ResultObject(IResultCode resultCode) {
        this(resultCode, (T) null, resultCode.getMessage());
    }

    private ResultObject(IResultCode resultCode, String msg) {
        this(resultCode, (T) null, msg);
    }

    private ResultObject(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private ResultObject(IResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private ResultObject(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.code == code;
    }

//    public static boolean isSuccess(@Nullable ResultObject<?> result) {
//        return (Boolean)Optional.ofNullable(result).map((x) -> {
//            return ObjectUtil.nullSafeEquals(ResultCode.SUCCESS.code, x.code);
//        }).orElse(Boolean.FALSE);
//    }

//    public static boolean isNotSuccess(@Nullable ResultObject<?> result) {
//        return !isSuccess(result);
//    }

    public static <T> ResultObject<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> ResultObject<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> ResultObject<T> data(int code, T data, String msg) {
        return new ResultObject(code, data, data == null ? "暂无承载数据" : msg);
    }

    public static <T> ResultObject<T> success(String msg) {
        return new ResultObject(ResultCode.SUCCESS, msg);
    }

    public static <T> ResultObject<T> success(IResultCode resultCode) {
        return new ResultObject(resultCode);
    }

    public static <T> ResultObject<T> success(IResultCode resultCode, String msg) {
        return new ResultObject(resultCode, msg);
    }

    public static <T> ResultObject<T> fail(String msg) {
        return new ResultObject(ResultCode.FAILURE, msg);
    }

    public static <T> ResultObject<T> fail(int code, String msg) {
        return new ResultObject(code, (Object)null, msg);
    }

    public static <T> ResultObject<T> fail(IResultCode resultCode) {
        return new ResultObject(resultCode);
    }

    public static <T> ResultObject<T> fail(IResultCode resultCode, String msg) {
        return new ResultObject(resultCode, msg);
    }

    public static <T> ResultObject<T> status(boolean flag) {
        return flag ? success("操作成功") : fail("操作失败");
    }

    public int getCode() {
        return this.code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultObject(code=" + this.getCode() + ", success=" + this.isSuccess() + ", data=" + this.getData() + ", msg=" + this.getMsg() + ")";
    }

    public ResultObject() {
    }
}
