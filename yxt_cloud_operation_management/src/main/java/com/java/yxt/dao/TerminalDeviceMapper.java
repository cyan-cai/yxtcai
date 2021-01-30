package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.vo.TerminalDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 终端设备管理表dao
 * @author 蔡家明
 */
@Mapper
public interface TerminalDeviceMapper {
    /**
     * 分页查询
     * @param terminalDeviceVo
     * @param page
     * @return
     */
    List<TerminalDeviceVo> select(Page page, @Param("terminalDeviceVo")TerminalDeviceVo terminalDeviceVo);

    /**
     * 不分页查询
     * @param terminalDeviceVo
     * @return
     */
    List<TerminalDeviceVo> selectAll(@Param("terminalDeviceVo")TerminalDeviceVo terminalDeviceVo);

    /**
     * 插入
     * @param terminalDeviceVo
     * @return
     */
    @SetCreaterName
    int insert(TerminalDeviceVo terminalDeviceVo);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatch(List<TerminalDeviceVo> list);

    /**
     * 更新
     * @param terminalDeviceVo
     * @return
     */
    int update(TerminalDeviceVo terminalDeviceVo);

    /**
     * 批量提交审核
     * @param list
     * @return
     */
    int updateBatch(List<TerminalDeviceVo> list);

    /**
     * 删除
     * @param terminalDeviceVo
     * @return
     */
    int delete(TerminalDeviceVo terminalDeviceVo);

    /**
     * 逻辑删除
     * @param terminalDeviceVo
     * @return
     */
    int updateStatus(TerminalDeviceVo terminalDeviceVo);

    /**
     * 根据IMEI查询终端设备
     * @param terminalDeviceVo
     * @return
     */
    TerminalDeviceVo selectByIMEI(TerminalDeviceVo terminalDeviceVo);

    /**
     * 根据MEID查询终端设备
     * @param terminalDeviceVo
     * @return
     */
    TerminalDeviceVo selectByMEID(TerminalDeviceVo terminalDeviceVo);

    /**
     * 根据IMEI和MEID查询终端设备
     * @param terminalDeviceVo
     * @return
     */
    TerminalDeviceVo selectByIMEIAndMEID(TerminalDeviceVo terminalDeviceVo);
}
