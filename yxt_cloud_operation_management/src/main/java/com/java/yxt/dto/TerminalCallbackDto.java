package com.java.yxt.dto;

import com.java.yxt.po.TerminalCallbackPo;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户回调关系实体类
 * @author zanglei
 */
@Data
public class TerminalCallbackDto extends TerminalCallbackPo implements Serializable {

    /**
     *
     *终端号码
     **/
    private String msisdn;


    /**
     *服务类型
     */
    private String apiCategory;

}