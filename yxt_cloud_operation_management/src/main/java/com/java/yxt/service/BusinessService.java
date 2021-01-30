package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalBlackVo;
import com.java.yxt.vo.TerminalWhiteVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务标识处理
 * @author zanglei
 */
public interface BusinessService {
    /**
     * 插入业务标识
     * @param serviceVo
     * @return
     */
    int insert(ServiceVo serviceVo);

    /**
     * 分页查询业务标识信息
     * @param page
     * @param serviceVo
     * @return
     */
    List<ServiceVo> select(Page<?> page, ServiceVo serviceVo);

    /**
     * 订阅关系分页查询
     * @param page
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectSubscribe(Page<?> page, ServiceVo serviceVo);
    /**
     * 不分页查询业务标识信息
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectAll(ServiceVo serviceVo);
    /**
     * 未关联终端白名单的业务标识
     * @param terminalWhiteVo
     * @return
     */
    List<ServiceVo> unRelationWhiteServiceCode(TerminalWhiteVo terminalWhiteVo);


    /**
     * 未关联sp客户黑名单的业务标识
     * @param terminalBlackVo
     * @return
     */
    List<ServiceVo> unRelationBlackServiceCode(TerminalBlackVo terminalBlackVo);

    /**
     * 查询有效的业务标识
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectEnableBusiness(ServiceVo serviceVo);

    /**
     * 更新
     * @param serviceVo
     * @return
     */
    int update(ServiceVo serviceVo);

    /**
     * 单条查询
     * @param serviceVo
     * @return
     */
    ServiceVo selectById(ServiceVo serviceVo);

    /**
     * 根据客户id查询关联的业务标识
     * @param serviceVo
     * @return
     */
    List<ServiceVo> selectByCustomerId(ServiceVo serviceVo);

    /**
     * 业务标识到期时间状态修改
     * @param serviceCodes
     * @return
     */
    int updateDisableStatus(@Param("serviceCodes")List<String> serviceCodes);
}
