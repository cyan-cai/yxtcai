package com.java.yxt.service.impl;

import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.dao.DeviceInfoMapper;
import com.java.yxt.dao.TerminalAuditMapper;
import com.java.yxt.dao.TerminalDeviceMapper;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.po.DeviceInfoPo;
import com.java.yxt.service.TerminalAuditService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.TerminalDeviceAuditDetailVo;
import com.java.yxt.vo.TerminalDeviceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
/**
 * TerminalAuditServiceImpl
 *
 * @author 蔡家明
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.service.impl
 * @date 2020/12/10
 */
@Service
@Slf4j
public class TerminalAuditServiceImpl implements TerminalAuditService {
    @Autowired
    TerminalAuditMapper terminalAuditMapper;

    @Autowired
    TerminalDeviceMapper terminalDeviceMapper;

    @Autowired
    DeviceInfoMapper deviceInfoMapper;
    /**
     * 审核详情
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    @Override
    public List<TerminalDeviceAuditDetailVo> selectAuditDetail(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        return terminalAuditMapper.selectAll(terminalDeviceAuditDetailVo);
    }

    /**
     * 审核通过
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int approve(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        //更新设备审核状态
        TerminalDeviceVo terminalDeviceVo=new TerminalDeviceVo();
        terminalDeviceVo.setId(terminalDeviceAuditDetailVo.getAuditId());
        terminalDeviceVo.setAuditStatus(3);
        terminalDeviceMapper.update(terminalDeviceVo);
        //新增审核详情
        terminalDeviceAuditDetailVo.setAuditResult(1);
        terminalDeviceAuditDetailVo.setAuditName(terminalDeviceAuditDetailVo.getCreaterName());
        terminalDeviceAuditDetailVo.setAuditTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalDeviceAuditDetailVo.setRemark("已通过审核");
        terminalDeviceAuditDetailVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        //同步信息至核心网
        DeviceInfoPo deviceInfoPo=new DeviceInfoPo();
        deviceInfoPo.setOperate(1);
        deviceInfoPo.setImei(terminalDeviceAuditDetailVo.getImei());
        deviceInfoPo.setDesc(terminalDeviceAuditDetailVo.getTerminalFactory());
        deviceInfoMapper.insert(deviceInfoPo);
        return terminalAuditMapper.insert(terminalDeviceAuditDetailVo);
    }

    /**
     * 批量审核通过
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveBatch(List<TerminalDeviceAuditDetailVo> list, HttpServletRequest request) {
        //排除不允许通过的状态
        Iterator<TerminalDeviceAuditDetailVo> itr = list.iterator();
        while (itr.hasNext()) {
            TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo = itr.next();
            if(terminalDeviceAuditDetailVo.getAuditStatus()!= 2 ) {
                itr.remove();
            }
        }
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        int count=0;
        for (TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo : list) {
            terminalDeviceAuditDetailVo.setAuditId(terminalDeviceAuditDetailVo.getId());
            terminalDeviceAuditDetailVo.setCreaterId(String.valueOf(admin.getAdminId()));
            terminalDeviceAuditDetailVo.setCreaterName(String.valueOf(admin.getRealname()));
            terminalDeviceAuditDetailVo.setTenantId(String.valueOf(admin.getSiteId()));
            terminalDeviceAuditDetailVo.setOrgId(String.valueOf(admin.getOrgId()));
            approve(terminalDeviceAuditDetailVo);
            count++;
        }
        return count;
    }

    /**
     *审核驳回
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int reject(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        //更新设备审核状态
        TerminalDeviceVo terminalDeviceVo=new TerminalDeviceVo();
        terminalDeviceVo.setId(terminalDeviceAuditDetailVo.getAuditId());
        terminalDeviceVo.setAuditStatus(4);
        terminalDeviceMapper.update(terminalDeviceVo);
        //新增审核详情
        terminalDeviceAuditDetailVo.setAuditResult(0);
        terminalDeviceAuditDetailVo.setAuditName(terminalDeviceAuditDetailVo.getCreaterName());
        terminalDeviceAuditDetailVo.setAuditTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalDeviceAuditDetailVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalAuditMapper.insert(terminalDeviceAuditDetailVo);
    }

    /**
     *批量审核驳回
     * @param list
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rejectBatch(List<TerminalDeviceAuditDetailVo> list, HttpServletRequest request) {
        //排除不允许驳回的状态
        Iterator<TerminalDeviceAuditDetailVo> itr = list.iterator();
        while (itr.hasNext()) {
            TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo = itr.next();
            if(terminalDeviceAuditDetailVo.getAuditStatus()!= 2 ) {
                itr.remove();
            }
        }
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        int count=0;
        for (TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo : list) {
            terminalDeviceAuditDetailVo.setAuditId(terminalDeviceAuditDetailVo.getId());
            terminalDeviceAuditDetailVo.setCreaterId(String.valueOf(admin.getAdminId()));
            terminalDeviceAuditDetailVo.setCreaterName(String.valueOf(admin.getRealname()));
            terminalDeviceAuditDetailVo.setTenantId(String.valueOf(admin.getSiteId()));
            terminalDeviceAuditDetailVo.setOrgId(String.valueOf(admin.getOrgId()));
            reject(terminalDeviceAuditDetailVo);
            count++;
        }
        return count;
    }

    /**
     *批准删除
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int approveDelete(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        //同步信息至核心网
        DeviceInfoPo deviceInfoPo=new DeviceInfoPo();
        deviceInfoPo.setOperate(2);
        deviceInfoPo.setImei(terminalDeviceAuditDetailVo.getImei());
        deviceInfoPo.setDesc(terminalDeviceAuditDetailVo.getTerminalFactory());
        deviceInfoMapper.insert(deviceInfoPo);
        //新增审核详情
        terminalDeviceAuditDetailVo.setAuditResult(1);
        terminalDeviceAuditDetailVo.setAuditName(terminalDeviceAuditDetailVo.getCreaterName());
        terminalDeviceAuditDetailVo.setAuditTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalDeviceAuditDetailVo.setRemark("已通过审核");
        terminalDeviceAuditDetailVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalAuditMapper.insert(terminalDeviceAuditDetailVo);
        //删除终端设备
        TerminalDeviceVo terminalDeviceVo=new TerminalDeviceVo();
        terminalDeviceVo.setId(terminalDeviceAuditDetailVo.getAuditId());
        return terminalDeviceMapper.updateStatus(terminalDeviceVo);
    }

    /**
     *驳回删除
     * @param terminalDeviceAuditDetailVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int rejectDelete(TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        //将设备状态置为通过状态
        TerminalDeviceVo terminalDeviceVo=new TerminalDeviceVo();
        terminalDeviceVo.setAuditStatus(3);
        terminalDeviceVo.setId(terminalDeviceAuditDetailVo.getAuditId());
        terminalDeviceMapper.update(terminalDeviceVo);
        //新增审核详情
        terminalDeviceAuditDetailVo.setAuditResult(0);
        terminalDeviceAuditDetailVo.setAuditName(terminalDeviceAuditDetailVo.getCreaterName());
        terminalDeviceAuditDetailVo.setAuditTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalDeviceAuditDetailVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalAuditMapper.insert(terminalDeviceAuditDetailVo);
    }
}
