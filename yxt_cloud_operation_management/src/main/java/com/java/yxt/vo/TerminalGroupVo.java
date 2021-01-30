package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.TerminalGroupPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-19 10:54
 */
@Slf4j
@Data
public class TerminalGroupVo extends TerminalGroupPo {

    /**
     * 终端id集合
     */
    private List<String> terminalIdList;


    /**
     *终端号码
     */
    private String msisdn;

    /**
     * 终端厂商
     */
    private String terminalFactory;

    /**
     * 开始页
     */
    private Integer current;

    /**
     * 每页显示条数
     */
    private Integer size;


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
