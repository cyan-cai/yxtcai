package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.ServicePo;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalBlackVo;
import com.java.yxt.vo.TerminalWhiteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务标识表 dao层
 * @author zanlgie
 */
@Mapper
public interface ServiceMapper extends MyBaseMapper<ServicePo>{
    /**
     * 删除
     * @param serviceVo
     * @return
     */
    int deleteByPrimaryKey(ServiceVo serviceVo);

    /**
     * 插入
     * @param serviceVo
     * @return
     */
    int insert(ServiceVo serviceVo);

    /**
     * 分页查询
     * @param page
     * @param serviceVo
     * @return
     */
    List<ServiceVo> select(Page<?> page, @Param("serviceVo") ServiceVo serviceVo);

    /**
     * 订阅关系查询
     * @param page
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectSubscribe(Page<?> page, @Param("serviceVo") ServiceVo serviceVo);

    /**
     * 查询所有不分页
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectAll(ServiceVo serviceVo);

    /**
     * 查询已订购客户
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectByCustomerandProduct(ServiceVo serviceVo);
    /**
     * 未关联终端白名单的业务标识
     * @param terminalWhiteVo
     * @return
     */

    List<ServiceVo> unRelationWhiteServiceCode(TerminalWhiteVo terminalWhiteVo);

    /**
     * 所有当前终端的业务标识
     * @param terminalWhiteVo
     * @return
     */

    List<ServiceVo> selectTerminalServiceCode(TerminalWhiteVo terminalWhiteVo);


    /**
     * SP客户未被关联的黑名单的业务标识
     * @param terminalBlackVo
     * @return
     */

    List<ServiceVo> unRelationBlackServiceCode(TerminalBlackVo terminalBlackVo);

    /**
     * 查询有效的业务标识
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectEnableBusinewss(ServiceVo serviceVo);

    /**
     * 单条
     * @param serviceVo
     * @return
     */
    ServiceVo selectById(ServiceVo serviceVo);


    /**
     * 更新
     * @param serviceVo
     * @return
     */
    int update(ServiceVo serviceVo);

    /**
     * 查询已被订购的销售品
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectBySaleCode(ServiceVo serviceVo);

    /**
     * 根据客户姓名模糊查询状态正常的销售品
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectEnableBusinewssByCustomerName(ServiceVo serviceVo);
}