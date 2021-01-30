package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.vo.RelationTerminalVo;
import com.java.yxt.vo.TerminalVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 终端用户管理
 * @author zanglei
 */

public interface TerminalService {

    /**
     * 插入
     * @param terminalVo
     * @return
     */
    int insert(TerminalVo terminalVo);

    /**
     * 导入插入
     * @param terminalVo
     * @return
     */
    @SetCreaterName
    int insertupload(TerminalVo terminalVo);

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
     * 终端关联客户
     * @param relationTerminalVo
     * @return
     */
    int relationCustomer(RelationTerminalVo relationTerminalVo);

    /**
     * 未关联关联终端
     * @param page
     * @param terminalVo
     * @return
     */
    List<TerminalVo> unRelationCustomer(Page<TerminalVo> page,TerminalVo terminalVo);

    /**
     * 分页查询
     * @param terminalVo
     * @param page
     * @return
     */
    List<TerminalVo> select(Page page, TerminalVo terminalVo);

    /**
     * 不分页查询
     * @param terminalVo
     * @return
     */
    List<TerminalVo> selectAll(TerminalVo terminalVo);

    /**
     * 根据终端号码查询对应的客户和终端信息
     * @param terminalVo
     * @return
     */
    List<TerminalVo> selectcustomerbymsisdn(TerminalVo terminalVo);

    /**
     * 导入终端用户
     * @param file
     * @param createrId
     * @param httpServletResponse
     * @param request
     * @return Response
     */
    Response uploadTerminalUser(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request);

    /**
     * 导出记录
     * @param terminalVo
     * @param response
     */
    void export(TerminalVo terminalVo, HttpServletResponse response);

    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    void exportError(String key,HttpServletResponse response);
    /**
     * 根据终端号码查询对应的客户和终端信息(去重)
     * @param terminalVo
     * @return
     */
    List<TerminalVo> selectdistinctcustomerbymsisdn(TerminalVo terminalVo);

}
