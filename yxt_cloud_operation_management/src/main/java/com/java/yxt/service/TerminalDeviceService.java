package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.vo.TerminalDeviceVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 终端设备管理
 * @author 蔡家明
 */

public interface TerminalDeviceService {

    /**
     * 分页查询
     * @param terminalDeviceVo
     * @param page
     * @return
     */
    List<TerminalDeviceVo> select(Page page, TerminalDeviceVo terminalDeviceVo,HttpServletRequest request);

    /**
     * 不分页查询
     * @param terminalDeviceVo
     * @return
     */
    List<TerminalDeviceVo> selectAll(TerminalDeviceVo terminalDeviceVo);

    /**
     * 插入
     * @param terminalDeviceVo
     * @return
     */
    int insert(TerminalDeviceVo terminalDeviceVo,HttpServletRequest request);

    /**
     * 更新
     * @param terminalDeviceVo
     * @return
     */
    int update(TerminalDeviceVo terminalDeviceVo);

    /**
     * 批量更新
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
     * 导入终端设备
     * @return void
     * @param file
     * @param createrId
     * @return Response
     */
    Response uploadTerminalDevice(MultipartFile file, String createrId,HttpServletResponse response,HttpServletRequest request);

    /**
     * 导出记录
     * @param terminalDeviceVo
     * @param response
     */
    void export(TerminalDeviceVo terminalDeviceVo, HttpServletResponse response);

    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    void exportError(String key,HttpServletResponse response);

    /**
     * 同步设备信息至核心网
     * @return String
     */
    String updateInfo();

    /**
     * 清空设备信息管理表
     */
     int clearInfo();
    /**
     * 检查用户权限判断是否是终端厂商
     * @param  request
     * @return Response
     */
    Response checkUser(HttpServletRequest request);
}
