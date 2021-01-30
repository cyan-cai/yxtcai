package com.java.yxt.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.po.TerminalPo;
import com.java.yxt.vo.TerminalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 终端用户表dao
 * @author zanglei
 */
@Mapper
public interface TerminalMapper extends MyBaseMapper<TerminalPo>{
    /**
     * 删除
     * @param terminalVo
     * @return
     */
    int deleteByPrimaryKey(TerminalVo terminalVo);

    /**
     * 插入
     * @param terminalVo
     * @return
     */
    @SetCreaterName
    int insert(TerminalVo terminalVo);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(List<TerminalVo> list);

    /**
     * 更新插入
     * @param terminalVo
     * @return
     */
    @SetCreaterName
    int insertupload(TerminalVo terminalVo);

    /**
     * 分页查询
     * @param terminalVo
     * @param page
     * @return
     */
    List<TerminalVo> select(Page page, @Param("terminalVo") TerminalVo terminalVo);

    /**
     * 查询
     * @param terminalVo
     * @return
     */
    List<TerminalVo> selectAll(@Param("terminalVo") TerminalVo terminalVo);

    /**
     * 分页查询 直接封装page
     * @param terminalVo
     * @param page
     * @return
     */
    IPage<List<TerminalVo>> queryTerminalInfo(Page<?> page, @Param("terminalVo") TerminalVo terminalVo);

   /**
     * 查询
     * @param terminalVo
     * @return
     */
    List<TerminalVo> selectcustomerbymsisdn(TerminalVo terminalVo);

    /**
     * 查询(去重)
     * @param terminalVo
     * @return
     */
    List<TerminalVo> selectdistinctcustomerbymsisdn(TerminalVo terminalVo);

    /**
     * 用户与客户绑定关系查询
     * @param terminalVo
     * @return
     */
    TerminalVo selectterminalbymsisdnandcustomername(TerminalVo terminalVo);

    /**
     * 根据用户姓名查询
     * @param terminalVo
     * @return
     */
    TerminalVo selectterminalbyname(TerminalVo terminalVo);

    /**
     * 根据用户姓名和msisdn查询
     * @param terminalVo
     * @return
     */
    TerminalVo selectterminalbynameandmsisdn(TerminalVo terminalVo);
    /**
     * 用户查询
     * @param terminalVo
     * @return
     */
    TerminalVo selectterminalbymsisdn(TerminalVo terminalVo);
    /**
     * 更新
     * @param terminalVo
     * @return
     */
    int update(TerminalVo terminalVo);
    /**
     * 根据msisdn更新
     * @param terminalVo
     * @return
     */
    int updateBymsisdn(TerminalVo terminalVo);

    /**
     *未关联终端
     * @return
     */
    List<TerminalVo> unRelationCustomer(Page<TerminalVo> page,@Param("terminalVo")TerminalVo terminalVo);

    /**
     * 终端绑定客户
     * @param relationTerminalVos
     * @param customerId
     * @return
     */
   int relationCustomer(@Param("relationTerminalVos")List<TerminalVo> relationTerminalVos,
                                                @Param("customerId")String customerId);

    /**
     * 把对应的客户的关联终端置null
     * @param unRelationTerminalVos
     * @return
     */
    int updateCustomerIdNull(@Param("unRelationTerminalVos")List<TerminalVo> unRelationTerminalVos);


}