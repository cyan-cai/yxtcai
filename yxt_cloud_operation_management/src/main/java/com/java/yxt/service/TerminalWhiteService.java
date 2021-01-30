package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.base.Response;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalWhiteVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 白名单接口
 * @author zanglei
 */
public interface TerminalWhiteService {
    /**
     * 插入
     * @param terminalWhiteVo
     * @return
     */
    int insert(TerminalWhiteVo terminalWhiteVo) throws JsonProcessingException;

    /**
     * 更新
     * @param terminalWhiteVo
     * @return
     */
    int update(TerminalWhiteVo terminalWhiteVo) throws JsonProcessingException;

    /**
     * 分页查询
     * @param page
     * @param terminalWhiteVo
     * @return
     */
    List<TerminalWhiteVo> select(Page<?> page, TerminalWhiteVo terminalWhiteVo);

    /**
     * 不分页查询
     * @param terminalWhiteVo
     * @return
     */
    List<TerminalWhiteVo> selectAll(TerminalWhiteVo terminalWhiteVo);
    
    /**
     * 导入终端用户白名单
     * @return void
     * @param file
     * @param createrId
     * @param httpServletResponse
     * @return Response
     */

    Response uploadterminalwhite(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request);
    
    /**
     * 导出记录
     * @param terminalWhiteVo
     * @param response
     */
    
    void export(TerminalWhiteVo terminalWhiteVo, HttpServletResponse response);

    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    void exportError(String key,HttpServletResponse response);

    /**
     * 查询有效业务标识
     * @param terminalWhiteVo
     * @return
     */
    List<ServiceVo> selectServiceCode(TerminalWhiteVo terminalWhiteVo);
}
