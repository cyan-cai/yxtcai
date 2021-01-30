package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.BasePo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DeviceInfoVo extends BasePo {
    private static final long serialVersionUID = 2L;
    /**
     * 同步地址
     */
    private String dirName;
    /**
     * 同步策略
     */
    private String frequency;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{}对象转jsonstring失败：{}",this.getClass().getSimpleName(),e.getMessage());
        }
        return null;
    }
}
