package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.SecretKeyHistoryMapper;
import com.java.yxt.dao.SecretKeyMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.service.SecretKeyService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.SecretKeyHistoryVo;
import com.java.yxt.vo.SecretKeyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * TerminalDeviceServiceImpl
 *
 * @author 蔡家明
 * @version V1.0
 * @description 密钥管理
 * @Package com.java.yxt.service.impl
 * @date 2020/1/12
 */
@Service
@Slf4j
public class SecretKeyServiceImpl implements SecretKeyService {

    @Autowired
    SecretKeyMapper secretKeyMapper;

    @Autowired
    SecretKeyHistoryMapper secretKeyHistoryMapper;

    /**
     * 分页查询
     * @param secretKeyVo
     * @param page
     * @return
     */
    @Override
    public List<SecretKeyVo> select(Page page, SecretKeyVo secretKeyVo) {
        return secretKeyMapper.select(page,secretKeyVo);
    }

    /**
     * 不分页查询
     * @param secretKeyVo
     * @return
     */
    @Override
    public List<SecretKeyVo> selectAll(SecretKeyVo secretKeyVo) {
        return secretKeyMapper.selectAll(secretKeyVo);
    }

    /**
     * 新增
     * @param secretKeyVo
     * @return
     */
    @Override
    @SetCreaterName
    public int insert(SecretKeyVo secretKeyVo) {
        //查看客户是否已有密钥
        if(secretKeyMapper.selectByCustomerCode(secretKeyVo)!=null){
            log.warn("当前客户已有密钥！");
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_SECRET_KEY_EXISTS);
        }
        secretKeyVo.setKeyStatus(0);
        secretKeyVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return secretKeyMapper.insert(secretKeyVo);
    }

    /**
     * 更新
     * @param secretKeyVo
     * @return
     */
    @Override
    @SetCreaterName
    public int update(SecretKeyVo secretKeyVo) {
        //密钥生效时不允许修改
        if(secretKeyVo.getKeyStatus()==1){
            log.warn("当前密钥状态为有效，请禁用后再修改！");
            throw new MySelfValidateException(ValidationEnum.SECRET_KEY_STATUS_ERROR);
        }
        secretKeyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return secretKeyMapper.update(secretKeyVo);
    }

    /**
     * 删除
     * @param secretKeyVo
     * @return
     */
    @Override
    public int delete(SecretKeyVo secretKeyVo) {
        return secretKeyMapper.delete(secretKeyVo);
    }

    /**
     * 禁用/禁用密钥
     * @param secretKeyVo
     * @return
     */
    @Override
    @SetCreaterName
    public int enableAndDisable(SecretKeyVo secretKeyVo) {
        secretKeyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return secretKeyMapper.update(secretKeyVo);
    }


    /**
     * 启用
     * @param secretKeyVo
     * @return
     */
    @Override
    @SetCreaterName
    public int enable(SecretKeyVo secretKeyVo) {
//        //查询当前客户是否存在有效密钥
//        List<SecretKeyVo> list=secretKeyMapper.selectValidByCustomerCode(secretKeyVo);
//        if(ValidateUtil.isNotEmpty(list)){
//            log.warn("一个客户下只能有一个生效的密钥！");
//            throw new MySelfValidateException(ValidationEnum.SECRET_KEY_EXISTS);
//        }
//        //新增密钥历史记录
//        SecretKeyHistoryVo secretKeyHistoryVo=new SecretKeyHistoryVo();
//        secretKeyHistoryVo.setKeyId(secretKeyVo.getId());
//        secretKeyHistoryVo.setKeyVersion(secretKeyVo.getKeyVersion());
//        secretKeyHistoryVo.setSecretKey(secretKeyVo.getSecretKey());
//        secretKeyHistoryVo.setKeyStartTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
//        secretKeyHistoryMapper.insert(secretKeyHistoryVo);
        //修改密钥状态
        secretKeyVo.setKeyStatus(1);
        secretKeyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return secretKeyMapper.update(secretKeyVo);
    }


    /**
     * 禁用
     * @param secretKeyVo
     * @return
     */
    @Override
    @SetCreaterName
    public int disable(SecretKeyVo secretKeyVo) {
//        //修改当前客户密钥历史记录,设置失效时间
//        SecretKeyHistoryVo secretKeyHistoryVo=new SecretKeyHistoryVo();
//        secretKeyHistoryVo.setKeyId(secretKeyVo.getId());
//        secretKeyHistoryVo.setKeyEndTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
//        secretKeyHistoryMapper.update(secretKeyHistoryVo);
        //修改密钥状态
        secretKeyVo.setKeyStatus(0);
        secretKeyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return secretKeyMapper.update(secretKeyVo);
    }

    /**
     * 分页查询密钥历史记录
     * @param secretKeyVo
     * @param page
     * @return
     */
    @Override
    public List<SecretKeyHistoryVo> selectKeyHistory(Page page, SecretKeyVo secretKeyVo) {
        return secretKeyHistoryMapper.select(page,secretKeyVo);
    }

    /**
     * 不分页查询密钥历史记录
     * @param secretKeyVo
     * @return
     */
    @Override
    public List<SecretKeyHistoryVo> selectAllKeyHistory(SecretKeyVo secretKeyVo) {
        return secretKeyHistoryMapper.selectAll(secretKeyVo);
    }
}
