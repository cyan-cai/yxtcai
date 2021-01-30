package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * ApiPo
 *
 * @author zanglei
 * @version V1.0
 * @description api表po
 * @Package com.java.yxt.po
 * @date 2020/9/11
 */
@Data
@Slf4j
public class ApiPo extends BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * url地址
     */
    private String url;
    /**
     * api 名称
     */
    private String name;
    /**
     * api 描述
     */
    private String description;
    /**
     *api 程序名称
     */
    private String apiProgramName;
    /**
     * api 服务类型
     */
    private String apiCategory;
    /**
     * 外部路径
     */
    private String extroPath;
    /**
     * 内部路径
     */
    private String internalPath;
    /**
     * 微服务名称
     */
    private String microName;
    /**
     * 微服务程序名称
     */
    private String microProgramName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人id
     */
    private String updaterId;
    /**
     * 修改时间
     */
    private Date updateTime;

    @Override
    public String toString(){
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }
}
