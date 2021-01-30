package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 导入异常信息po
 * @author caijiaming
 */
@Data
@Slf4j
public class UpLoadErrorPo implements Serializable {
    /**
     *主键key
     */
    private String key;

    /**
     *
     * 保存文件字段
     */
    private String keyText;
    private static final long serialVersionUID = 1L;
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