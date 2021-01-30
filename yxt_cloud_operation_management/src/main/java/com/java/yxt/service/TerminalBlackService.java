package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalBlackVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 终端黑名单接口
 * @author zanglei
 */
public interface TerminalBlackService {

    /**
     * 插入
     * @param terminalBlackVo
     * @return
     */
    int insert(TerminalBlackVo terminalBlackVo);

    /**
     * 更新黑名单终端用户
     * @param terminalBlackVo
     * @return
     */
    int updateUser(TerminalBlackVo terminalBlackVo);


    /**
     * 更新黑名单SP应用平台
     * @param terminalBlackVo
     * @return
     */
    int updateSp(TerminalBlackVo terminalBlackVo);

    /**
     * 分页查询黑名单终端用户
     * @param page
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectUser(Page<?> page, TerminalBlackVo terminalBlackVo);

    /**
     * 分页查询黑名单SP用户
     * @param page
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectSp(Page<?> page, TerminalBlackVo terminalBlackVo);

    /**
     * 不分页查询
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectAll(TerminalBlackVo terminalBlackVo);

    /**
     * 启用禁用
     * @param terminalBlackVo
     * @return
     */
    int enableDisable(TerminalBlackVo terminalBlackVo);
	
	/**
     * 导入主被叫黑名单终端用户
     * @return void
     * @param file
     * @param createrId
     * @param httpServletResponse
     * @return Response
     */
    Response uploadTerminalBlackUser(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request);

    /**
     * 导入主被叫黑名单SP用户
     * @return void
     * @param file
     * @param createrId
     * * @param httpServletResponse
     * @return Response
     */
    Response  uploadTerminalBlackSp(MultipartFile file, String createrId,HttpServletResponse httpServletResponse,HttpServletRequest request);

    /**
     * 插入终端用户
     * @param terminalBlackVo
     * @return
     */
    void insertUser(TerminalBlackVo terminalBlackVo);

    /**
     * 插入SP应用平台用户
     * @param terminalBlackVo
     * @return
     */
    void insertSp(TerminalBlackVo terminalBlackVo);

    /**
     * 终端用户导出记录
     * @param terminalBlackVo
     * @param response
     */
    void exportUser(TerminalBlackVo terminalBlackVo, HttpServletResponse response);

    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    void exportUserError(String key,HttpServletResponse response);



    /**
     * SP应用平台导出记录
     * @param terminalBlackVo
     * @param response
     */
    void exportSp(TerminalBlackVo terminalBlackVo, HttpServletResponse response);

    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    void exportSpError(String key,HttpServletResponse response);


    /**
     * SP应用平台查询所有客户名称
     * @param terminalBlackVo
     * @return
     */
    List<TerminalBlackVo> selectByCustomerName(TerminalBlackVo terminalBlackVo);

    /**
     * SP应用平台查询有效业务标识
     * @param terminalBlackVo
     * @return
     */
    List<ServiceVo> selectServiceCode(TerminalBlackVo terminalBlackVo);
}
