package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.dto.CustomerDto;
import com.java.yxt.vo.CustomerVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户管理
 * @author zanglei
 */
public interface CustomerService {
    /**
     * 插入
     * @param customerVo
     * @return
     */
    int insert(CustomerVo customerVo);

    /**
     * 批量插入
     * @param customerVos
     * @return
     */
    int insertList(List<CustomerVo> customerVos);

    /**
     * 更新
     * @param customerVo
     * @return
     */
    int update(CustomerVo customerVo);

    /**
     * 更新用户状态
     * @param customerVo
     * @return
     */
    int updateStatus(CustomerVo customerVo);


    /**
     * 分页查询
     * @param page
     * @param customerVo
     * @return
     */
    List<CustomerVo> select(Page<CustomerVo> page,CustomerVo customerVo);


    /**
     * 查询
     * @param customerVo
     * @return
     */
    List<CustomerVo> selectAll(CustomerVo customerVo);

    /**
     * 查询所有有效的开通短报文的客户信息
     * @param customerVo
     * @return
     */
    List<CustomerVo> selectAllValidShortMsg(CustomerVo customerVo);

    /**
     * 根据终端号码分页查询客户信息
     * @param page
     * @param customerDto
     * @return
     */
    List<CustomerDto> selectByMsisdn(Page <?> page, CustomerDto customerDto);
    /**
     * 根据终端号码、服务类型分页查询客户信息
     * @param page
     * @param customerDto
     * @return
     */
    List<CustomerVo> selectCommon(Page <?> page, CustomerDto customerDto);

    /**
     * 文件导入
     * @param file
     * @param createrId
     * @param httpServletResponse
     */
    Response uploadCustomer(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request);

    /**
     * 文件导出
     * @param customerVo
     * @param response
     */
    void export(CustomerVo customerVo, HttpServletResponse response);


    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    void exportError(String key,HttpServletResponse response);
}
