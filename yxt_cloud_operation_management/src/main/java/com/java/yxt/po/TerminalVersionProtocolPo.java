package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;

import java.io.Serializable;

/**
 * @author zanglei
 * @version V1.0
 * @description 终端版本协议管理实体类
 * @Package com.java.yxt.po
 * @date 2021/1/18
 */
@Data
@Slf4j
public class TerminalVersionProtocolPo extends BasePo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;

    /**
     * 版本号
     */
    private String terminalVersion ;

    /**
     * 协议标识
     */
    private String protocolIdentity ;
    /**
     * 终端类型
     */
    private Integer terminalType ;

    /**
     *终端厂商
     */
    private String terminalFactory ;

    /**
     *协议描述
     */
    private String protocolDesc ;
    /**
     * 状态 1有效,0无效
     */
    private Integer status;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
            log.error("{} 对象转jsonString异常：", ClassUtils.getShortName(this.getClass()),e);
        }
        return null;
    }
}
