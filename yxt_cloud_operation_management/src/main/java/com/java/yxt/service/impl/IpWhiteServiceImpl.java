package com.java.yxt.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.CustomerMapper;
import com.java.yxt.dao.IpWhiteMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.service.IpWhiteService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.CustomerVo;
import com.java.yxt.vo.IpWhiteVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * IpWhiteServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.service.impl
 * @date 2020/9/24
 */
@Service
@Slf4j
public class IpWhiteServiceImpl implements IpWhiteService {

    @Autowired
    IpWhiteMapper ipWhiteMapper;
    @Autowired
    CustomerMapper customerMapper;

    /**
     * 插入
     * @param ipWhiteVo
     * @return
     */
    @Override
    public int insert(IpWhiteVo ipWhiteVo) {
        //查看客户状态，无效时不允许添加
        CustomerVo customerVo=new CustomerVo();
        customerVo.setId(ipWhiteVo.getCustomerId());
        customerVo=customerMapper.selectAll(customerVo).get(0);
        if(customerVo.getStatus()==0){
            log.error("ip白名单添加异常");
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_STATUS_INVALID);
        }
        ipWhiteVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        ipWhiteVo.setStatus((short) 1);
        IpWhiteVo ipWhiteVo1 = new IpWhiteVo();
        ipWhiteVo1.setCustomerId(ipWhiteVo.getCustomerId());
        ipWhiteVo1.setIps(ipWhiteVo.getIps());
        List<IpWhiteVo> ipWhiteVos = ipWhiteMapper.selectByContion(ipWhiteVo1);
        if(ValidateUtil.isNotEmpty(ipWhiteVos) && ipWhiteVos.size()>0){
            log.warn("当前客户：{},ip已存在，不允许重复添加",ipWhiteVo.getCustomerName());
            throw new MySelfValidateException(ValidationEnum.IPWHITE_IP_EXISTS);
        }
        return ipWhiteMapper.insert(ipWhiteVo);
    }

    /**
     * 更新
     * @param ipWhiteVo
     * @return
     */
    @Override
    public int update(IpWhiteVo ipWhiteVo) {
        ipWhiteVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        if(ipWhiteVo.getIps()!=null){
            //查询当前客户是否已拥有当前IP
            List<IpWhiteVo> list=ipWhiteMapper.selectByContion(ipWhiteVo);
            if(list.size()>0&&!ipWhiteVo.getId().equals(list.get(0).getId())){
                    log.warn("当前客户：{},ip已存在，不允许重复添加",ipWhiteVo.getCustomerName());
                    throw new MySelfValidateException(ValidationEnum.IPWHITE_IP_EXISTS);
            }
        }
        return ipWhiteMapper.update(ipWhiteVo);
    }

    /**
     * 分页查询
     * @param page
     * @param ipWhiteVo
     * @return
     */
    @Override
    public List<IpWhiteVo> select(Page page, IpWhiteVo ipWhiteVo) {
        return ipWhiteMapper.select(page,ipWhiteVo);
    }

    /**
     * 不分页查询
     * @param ipWhiteVo
     * @return
     */
    @Override
    public List<IpWhiteVo> selectAll(IpWhiteVo ipWhiteVo) {
        return ipWhiteMapper.selectAll(ipWhiteVo);
    }

    /**
     * 文件导出
     * @param ipWhiteVo
     * @return
     */
    @Override
    public void export(IpWhiteVo ipWhiteVo, HttpServletResponse response) {
        if(ValidateUtil.isNotEmpty(ipWhiteVo.getCreateTimeList())){
            ipWhiteVo.setCreateBeginTime((String)ipWhiteVo.getCreateTimeList().toArray()[0]);
            ipWhiteVo.setCreateEndTime((String)ipWhiteVo.getCreateTimeList().toArray()[1]);
        }
        List<IpWhiteVo> list=ipWhiteMapper.selectAll(ipWhiteVo);
        if(ValidateUtil.isNotEmpty(list)){
            String str1="1";
            list.stream().forEach(ipWhiteVo1 -> {
                if(ipWhiteVo1.getStatus()==Integer.parseInt(str1)){
                    ipWhiteVo1.setStatusStr("有效");
                }else{
                    ipWhiteVo1.setStatusStr("无效");
                }
            });
        }
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 设置列宽
        writer.setColumnWidth(0,30);
        writer.setColumnWidth(1,30);
        writer.setColumnWidth(2,30);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        // 修改别名
        writer.addHeaderAlias("ipProtocal","IP协议");
        writer.addHeaderAlias("ip","IP地址");
        writer.addHeaderAlias("customerName","客户名称");
        writer.addHeaderAlias("statusStr","白名单状态");
        writer.addHeaderAlias("createrName","创建人");
        writer.addHeaderAlias("createTime","创建时间");
        writer.write(list);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=ipwhite.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("客户信息导出异常：",e);
            throw new MySelfValidateException(ValidationEnum.EXPORTFILE_OUTPUTSTREAM_ERROR);
        }finally {
            writer.flush(out);
            // 关闭writer，释放内存
            writer.close();
        }
    }

}
