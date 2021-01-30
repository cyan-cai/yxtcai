package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.po.TerminalBlackPo;
import com.java.yxt.vo.TerminalBlackVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 终端黑名单dao
 * @author zanglei
 */
@Mapper
public interface TerminalBlackMapper extends MyBaseMapper<TerminalBlackPo>{
    /**
     * 删除
     * @param terminalBlackVo
     * @return
     */
    int delete(TerminalBlackVo terminalBlackVo);

    /**
     * 插入
     * @param terminalBlackVo
     * @return
     */
    @SetCreaterName
    int insert(TerminalBlackVo terminalBlackVo);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(List<TerminalBlackVo> list);
    /**
     * 检查终端限制是否已经存在
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> isTerminalExists(TerminalBlackVo terminalBlackVo);

    /**
     * 检查SP平台限制是否已经存在
     * @param terminalBlackVo
     * @return
     */
    TerminalBlackVo isTerminalSpExists(TerminalBlackVo terminalBlackVo);
    /**
     * 分页查询终端用户
     * @param page
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectUser(Page<?> page, @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 分页查询SP用户
     * @param page
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectSp(Page<?> page, @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 不分页查询
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectAll( @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 启用禁用
     * @param terminalBlackVo
     * @return
     */
    int enableDisable( @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 更新
     * @param terminalBlackVo
     * @return
     */
    int update(TerminalBlackVo terminalBlackVo);

    /**
     * SP应用平台查询所有客户名称
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectByCustomerName(@Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 根据客户名查找客户代码
     * @param customerName
     * @return
     */
    String selectCustomerCodeByCustomerName(String  customerName);

    /**
     * 精确查询SP用户
     * @param terminalBlackVo
     * @return
     */
    TerminalBlackVo selectTerminalBlackByContion( @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 根据客户编码查询客户主叫状态是否正常
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo>  selectTerminalBlackByCustomerCode( @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);

    /**
     * 根据终端号查询黑名单
     * @param terminalBlackVo
     * @return
     */
    TerminalBlackVo selectTerminalBlackByMsisdn( @Param("terminalBlackVo") TerminalBlackVo terminalBlackVo);
}