package com.java.yxt.feign.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.feign.vo
 * @date 2020/10/15
 */
@Data
@Slf4j
public class AddOrgVo {
    /**
     *管理员账号
     */
    private String manageAccount;
    /**
     *     管理员密码
     */
    private String managePassword;
    /**
     * 邮箱
     */
    private String email;

    /** 备注 */
    private String remark;

    /** 皮肤 */
    private String skin;

    /** 机构名称 */
    private String orgName;

    /** 租户标识 */
    private Long siteid;

    /** 联系方式 */
    private String phone;

    /** 联系人 */
    private String contacts;

    /** 机构ID */
    private Integer organizationId;

    /** 机构状态 */
    private Integer status;

    /** 机构类型 */
    private String orgType;

    /** 法人名称 */
    private String legalPerson;

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
