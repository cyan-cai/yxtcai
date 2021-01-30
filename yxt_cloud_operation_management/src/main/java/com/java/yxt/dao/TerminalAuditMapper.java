package com.java.yxt.dao;

import com.java.yxt.vo.TerminalDeviceAuditDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 终端审核管理表dao
 * @author 蔡家明
 */
@Mapper
public interface TerminalAuditMapper {
    /**
     * 不分页查询
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    List<TerminalDeviceAuditDetailVo> selectAll( TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);

    /**
     * 新增
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    int insert(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);

    /**
     * 删除
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    int delete(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);
}
