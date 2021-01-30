package com.java.yxt.service;

import com.java.yxt.vo.TerminalDeviceAuditDetailVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 终端设备审核管理
 * @author 蔡家明
 */
public interface TerminalAuditService {


    /**
     * 审核详情
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    List<TerminalDeviceAuditDetailVo> selectAuditDetail(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);

    /**
     * 审核通过
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    int approve(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);

    /**
     * 批量审核通过
     * @param list
     * @param request
     * @return
     */
    int approveBatch(List<TerminalDeviceAuditDetailVo> list, HttpServletRequest request);

    /**
     *审核驳回
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    int reject(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);

    /**
     *批量审核驳回
     * @param list
     * @param request
     */
    int rejectBatch(List<TerminalDeviceAuditDetailVo> list, HttpServletRequest request);

    /**
     *批准删除
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    int approveDelete(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);

    /**
     *驳回删除
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    int rejectDelete(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo);
}
