package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * TerminalVo
 *
 * @author caijiaming
 * @version V1.0
 * @Package com.java.yxt.vo
 * @date 2021/01/08
 */
@Data
@Slf4j
public class RelationTerminalVo  implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * 取消关联客户的终端
     */
    private List<TerminalVo> unRelationTerminalVos;

    /**
     * 关联客户的终端
     */
    private List<TerminalVo> relationTerminalVos;
    /**
     * 客户id
     */
    private String customerId;

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
