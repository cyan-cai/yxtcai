package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.TerminalFactoryPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class TerminalFactoryVo extends TerminalFactoryPo implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * 开始页
     */
    private Integer current;

    /**
     * 每页显示条数
     */
    private Integer size;

    /**
     * 厂商状态字符串
     */
    private String factoryStatusStr;


    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }
}
