package com.java.yxt.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.java.yxt.po.TerminalVersionProtocolPo;
import lombok.Data;

/**
 * @author zanglei
 * @version V1.0
 * @description 终端协议版本管理vo
 * @Package com.java.yxt.vo
 * @date 2021/1/18
 */
@Data
@TableName("mgt_terminal_version_protocolmanagement")
public class TerminalVersionProtocolVo extends TerminalVersionProtocolPo {
    /**
     * 起始页
     */
    @TableField(exist = false)
    private Integer current;
    /**
     * 每页条数
     */
    @TableField(exist = false)
    private Integer size;
}
