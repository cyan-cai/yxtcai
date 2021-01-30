package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.po.TerminalCallbackPo;
import com.java.yxt.vo.TerminalCallbackVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 回调关系接口
 * @author zanglei
 */
@Mapper
public interface TerminalCallbackMapper extends MyBaseMapper<TerminalCallbackPo>{
    /**
     * 删除
     * @param terminalCallbackServiceVo
     * @return
     */
    int delete(TerminalCallbackVo terminalCallbackServiceVo);

    /**
     * 插入
     * @param terminalCallbackServiceVo
     * @return
     */
    @SetCreaterName
    int insert(TerminalCallbackVo terminalCallbackServiceVo);

    /**
     * 分页查询
     * @param page
     * @param terminalCallbackVo
     * @return
     */
    List<TerminalCallbackVo> select(Page<?> page, @Param("terminalCallbackVo") TerminalCallbackVo terminalCallbackVo);

    /**
     * 查询不分页
     * @param terminalCallbackVo
     * @return
     */
    List<TerminalCallbackVo> selectAll(TerminalCallbackVo terminalCallbackVo);

    /**
     * 根据客户编码和服务类型查询回调地址
     * @param terminalCallbackVo
     * @return
     */
    Map  getCallbackUrlByCusCodeApiCategory(TerminalCallbackVo terminalCallbackVo);

    /**
     * 更新
     * @param terminalCallbackServiceVo
     * @return
     */
    int update(TerminalCallbackVo terminalCallbackServiceVo);
    /**
     * 更新
     * @param terminalCallbackServiceVo
     * @return
     */
    int updateByServiceAndTerminalId(TerminalCallbackVo terminalCallbackServiceVo);

    /**
     * 获取关联有效终端的回调关系
     * @param terminalCallbackServiceVo
     * @return
     */
    List<TerminalCallbackVo> getRelationCallbackTerminal(TerminalCallbackVo terminalCallbackServiceVo);
}