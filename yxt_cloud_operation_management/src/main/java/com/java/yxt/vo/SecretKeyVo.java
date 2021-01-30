package com.java.yxt.vo;

import com.java.yxt.po.SecretKeyPo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * SecretKeyVo
 *
 * @author caijiaming
 * @version V1.0
 * @Package com.java.yxt.vo
 * @date 2021/1/12
 */
@Slf4j
@Data
public class SecretKeyVo extends SecretKeyPo implements Serializable {
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
     * 客户名称
     */
    private String customerName;


}
