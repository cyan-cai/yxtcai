package com.example.rocketmq.basic;

import java.io.Serializable;

public interface ErrorCode extends Serializable {

    /**
     * 错误码
     *
     * @return
     */
    String getCode();

    /**
     * 错误信息
     *
     * @return
     */
    String getMsg();
}
