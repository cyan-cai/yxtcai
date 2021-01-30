package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.TerminalFactoryMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.feign.CustomerFeignService;
import com.java.yxt.feign.ResultObject;
import com.java.yxt.feign.vo.AddOrgVo;
import com.java.yxt.service.TerminalFactoryService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.TerminalFactoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TerminalFactoryServiceImpl
 *
 * @author 蔡家明
 * @version V1.0
 * @description 终端厂商管理
 * @Package com.java.yxt.service.impl
 * @date 2020/12/9
 */
@Service
@Slf4j
public class TerminalFactoryServiceImpl implements TerminalFactoryService {

    @Autowired
    TerminalFactoryMapper terminalFactoryMapper;

    @Autowired
    CustomerFeignService customerFeignService;
    /**
     * 分页查询
     * @param terminalFactoryVo
     * @param page
     * @return
     */
    @Override
    public List<TerminalFactoryVo> select(Page page, TerminalFactoryVo terminalFactoryVo) {
        return terminalFactoryMapper.select(page,terminalFactoryVo);
    }

    /**
     * 不分页查询
     * @param terminalFactoryVo
     * @return
     */
    @Override
    public List<TerminalFactoryVo> selectAll(TerminalFactoryVo terminalFactoryVo) {
        return terminalFactoryMapper.selectAll(terminalFactoryVo);
    }

    /**
     * 模糊查询状态正常的终端厂商
     * @param terminalFactoryVo
     * @return
     */
    @Override
    public List<TerminalFactoryVo> selectFactoryName(TerminalFactoryVo terminalFactoryVo) {
        return terminalFactoryMapper.selectFactoryName(terminalFactoryVo);
    }

    /**
     * 插入
     * @param terminalFactoryVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int insert(TerminalFactoryVo terminalFactoryVo) {
        //设置厂商状态
        terminalFactoryVo.setFactoryStatus(1);
        //厂商名称校验
        if(terminalFactoryMapper.selectByFactoryName(terminalFactoryVo)!=null){
            log.warn("终端厂商名称已存在：{}！",terminalFactoryVo.getFactoryName());
            throw  new MySelfValidateException(ValidationEnum.TERMINAL_FACTORY_NAME_EXISTS);
        }
        // 同步添加组织 新增厂商用户账号
        AddOrgVo addOrgVo = new AddOrgVo();
        addOrgVo.setOrgName(terminalFactoryVo.getFactoryName());
        addOrgVo.setContacts(terminalFactoryVo.getCorporationName());
        addOrgVo.setManageAccount(terminalFactoryVo.getFactoryPhoneNum());
        addOrgVo.setManagePassword("a597e81fc62f4e9af5e139bfe05a9163");
        addOrgVo.setPhone(terminalFactoryVo.getFactoryPhoneNum());
        addOrgVo.setOrgType("终端厂商");
        addOrgVo.setLegalPerson(terminalFactoryVo.getCorporationName());
        // 第三方调用需加如下设置
        /**
         request.setAttribute("isNeedToken","false");
         addOrgVo.setSiteid(1L);
         **/
        int count=0;
        ResultObject resultObject = customerFeignService.save(addOrgVo);
        if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
            // 组织添加成功再添加客户
            terminalFactoryVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            terminalFactoryVo.setId(String.valueOf(resultObject.getData()));
            count=terminalFactoryMapper.insert(terminalFactoryVo);
        }else{
            log.error("添加客户同步添加组织信息失败:{}",resultObject);
            throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
        }
        return count;
    }

    /**
     * 更新
     * @param terminalFactoryVo
     * @return
     */
    @Override
    @SetCreaterName
    public int update(TerminalFactoryVo terminalFactoryVo) {
        terminalFactoryVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        TerminalFactoryVo terminalFactoryVo1=terminalFactoryMapper.selectByFactoryName(terminalFactoryVo);
        //厂商名称校验
        if(terminalFactoryVo1!=null&&!(terminalFactoryVo1.getId().equals(terminalFactoryVo.getId()))){
            log.warn("终端厂商名称已存在：{}！",terminalFactoryVo.getFactoryName());
            throw  new MySelfValidateException(ValidationEnum.TERMINAL_FACTORY_NAME_EXISTS);
        }
        return terminalFactoryMapper.update(terminalFactoryVo);
    }

    /**
     * 删除
     * @param terminalFactoryVo
     * @return
     */
    @Override
    public int delete(TerminalFactoryVo terminalFactoryVo) {
        return terminalFactoryMapper.delete(terminalFactoryVo);
    }

    /**
     * 启用/注销
     * @param terminalFactoryVo
     * @return
     */
    @Override
    public int enableOrDisable(TerminalFactoryVo terminalFactoryVo) {
        if(terminalFactoryVo.getFactoryStatus()==0){
            //注销厂商删除组织
            ResultObject resultObject = customerFeignService.delete(Integer.parseInt(terminalFactoryVo.getId()));
            if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
                terminalFactoryVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            }else{
                log.error("注销终端厂商同步删除组织信息失败:{}",resultObject);
                throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
            }
        }else {
            //启用厂商开通组织
            ResultObject resultObject = customerFeignService.update(Integer.parseInt(terminalFactoryVo.getId()));
            if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
                // 组织添加成功再修改客户
                terminalFactoryVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            }else{
                log.error("修改客户同步添加组织信息失败:{}",resultObject);
                throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
            }
        }
        return terminalFactoryMapper.update(terminalFactoryVo);
    }
}
