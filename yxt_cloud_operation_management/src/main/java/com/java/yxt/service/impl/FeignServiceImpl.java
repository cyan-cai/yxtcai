package com.java.yxt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.java.yxt.base.Response;
import com.java.yxt.dao.CustomerMapper;
import com.java.yxt.dao.TerminalBlackMapper;
import com.java.yxt.dao.TerminalCallbackMapper;
import com.java.yxt.dao.TerminalMapper;
import com.java.yxt.dto.TermIdDto;
import com.java.yxt.dto.TermIdRecDto;
import com.java.yxt.dto.TerminalCallbackDto;
import com.java.yxt.feign.CustomerFeignService;
import com.java.yxt.service.*;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zanglei
 * @version V1.0
 * @description 外界接口业务处理
 * @Package com.java.yxt.service.impl
 * @date 2020/11/10
 */
@Service
@Slf4j
public class FeignServiceImpl {
    @Autowired
    CustomerService customerService;

    @Autowired
    TerminalCallbackService terminalCallbackService;

    @Autowired
    BusinessService businessService;

    @Autowired
    TerminalService terminalService;

    @Autowired
    CustomerFeignService customerFeignService;

    @Autowired
    TerminalCallbackMapper terminalCallbackMapper;

    @Autowired
    DictService dictService;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    TerminalMapper terminalMapper;

    @Autowired
    TerminalBlackMapper terminalBlackMapper;

    /**
     * 根据终端号码和服务类型查询客户信息
     * @param terminalCallbackDtos
     * @return
     */
    public List<CustomerVo> getCustomerInfoByMsisdnCategory(List<TerminalCallbackDto> terminalCallbackDtos){
        List<CustomerVo> customerVos1 = new ArrayList<>();
        if(terminalCallbackDtos.isEmpty()){
            return null;
        }
        for(TerminalCallbackDto terminalCallbackDto : terminalCallbackDtos){
            if(ValidateUtil.isEmpty(terminalCallbackDto.getMsisdn())){
                continue;
            }
            TerminalVo terminalVo = new TerminalVo();
            terminalVo.setMsisdn(terminalCallbackDto.getMsisdn());
            terminalVo.setUserStatus(1);;
            String terminalId = null;
            String serviceId = null;

            // 查询终端信息
            List<TerminalVo> terminalVos = terminalService.selectAll(terminalVo);
            if(ValidateUtil.isEmpty(terminalVos)){
               continue;
            }
            terminalId = terminalVos.get(0).getId();

            TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
            terminalCallbackVo.setApiCategory(terminalCallbackDto.getApiCategory());
            terminalCallbackVo.setTerminalId(terminalId);
            terminalCallbackVo.setStatus(1);
            // 查询回调关系表对应的serviceId
            List<TerminalCallbackVo> callbackVos = terminalCallbackService.select(null,terminalCallbackVo);
            if(ValidateUtil.isEmpty(callbackVos)){
                continue;
            }
            serviceId = callbackVos.get(0).getServiceId();

            // 查询业务标识表
            String customerCode = null;
            ServiceVo serviceVo = new ServiceVo();
            serviceVo.setId(serviceId);
            serviceVo.setStatus(1);
            ServiceVo serviceVo1 = businessService.selectById(serviceVo);
            if(ValidateUtil.isEmpty(serviceVo1)){
               continue;
            }
            customerCode = serviceVo1.getCustomerCode();

            // 查询客户信息表
            CustomerVo customerVo = new CustomerVo();
            customerVo.setCustomerCode(customerCode);
            customerVo.setStatus(1);
            List<CustomerVo> customerVos = customerService.selectAll(customerVo);
            if(ValidateUtil.isNotEmpty(callbackVos) && ValidateUtil.isNotEmpty(customerVos))
            {
                CustomerVo customerVo1 = customerVos.get(0);
                customerVo1.setServiceCode(serviceVo1.getServiceCode());
                customerVo1.setCallbackUrl(serviceVo1.getCallbackUrl());
                customerVo1.setCustomerCode(customerCode);
                customerVo1.setMsisdn(terminalCallbackDto.getMsisdn());
                customerVo1.setApiCategory(terminalCallbackDto.getApiCategory());
                customerVos1.add(customerVo1);
            }
        }
        return  customerVos1.size()>0? customerVos1:null;
    }

    /**
     * 获取开通saas客户的回调地址
     * @return
     */
    public Response getSaasCustomerAndMsisdnApiCategory(String apiCategory){
        JSONObject jsonObject = new JSONObject();

        // saas用户粗定位回调地址
        DictVo dictVo = new DictVo();
        dictVo.setType("saas_customer_rough_position");
        List<DictVo> dictVos = dictService.selectAll(dictVo);
        if(ValidateUtil.isNotEmpty(dictVos)){
            jsonObject.put("callbackUrl",dictVos.get(0).getDesc());
        }

        // 查询开通saas的有效状态的客户信息
        CustomerVo customerVo = new CustomerVo();
        customerVo.setStatus(1);
        customerVo.setSaas(1);
        List<CustomerVo> customerVos = customerService.selectAll(customerVo);

        if(ValidateUtil.isEmpty(customerVos)){
            return Response.successWithResult(null);
        }
        Map<String,List<TerminalCallbackVo>> map = new HashMap<>();
        customerVos.stream().forEach(customerVo1 -> {
            // 根据客户查询有效的业务标识
            ServiceVo serviceVo = new ServiceVo();
            serviceVo.setCustomerCode(customerVo1.getCustomerCode());
            List<ServiceVo> serviceVos = businessService.selectEnableBusiness(serviceVo);
            List<String> serviceIds =  serviceVos.stream().map(serviceVo1 -> serviceVo1.getId()).collect(Collectors.toList());
            if(ValidateUtil.isEmpty(serviceIds)){
                return;
            }
            // 根据业务标识表主键查询回调关系表有效终端和对应的服务类型
            TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
            terminalCallbackVo.setServiceIds(serviceIds);
            terminalCallbackVo.setApiCategory(apiCategory);
            List<TerminalCallbackVo> terminalCallbackVos = terminalCallbackMapper.getRelationCallbackTerminal(terminalCallbackVo);
            if(ValidateUtil.isEmpty(terminalCallbackVos)){
                return;
            }
            map.put(customerVo1.getCustomerCode(),terminalCallbackVos);
        });
        jsonObject.put("customerList",map);
        return Response.successWithResult(jsonObject.toJSONString());
    }

    /**
     * 根据终端号码和服务类型获取回调地址
     * @return
     */
    public Response getCallbackUrlByCusCodeApiCategory(String customerCode,String apiCategory){

        TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
        terminalCallbackVo.setApiCategory(apiCategory);
        terminalCallbackVo.setCustomerCode(customerCode);
        Map<String,Object> map = terminalCallbackMapper.getCallbackUrlByCusCodeApiCategory(terminalCallbackVo);
        return Response.successWithResult(JsonUtil.map2json(map));
    }

    /**
     * 客户发送终端
     * @param termIdDto
     * @return
     */
    public Response selectTermIds(TermIdDto termIdDto){
       //根据客户编码查询客户
        CustomerVo customerVo=new CustomerVo();
        customerVo.setCustomerCode(termIdDto.getCustomerCode());
        customerVo=customerMapper.selectCustomerByCustomerCode(customerVo);
        if(customerVo==null){
            termIdDto.setStatus(0);
            termIdDto.setSrcBlackServiceNumber(termIdDto.getSpServiceNumber());
            return new Response(0,"客户不存在",termIdDto);
        }
        if(customerVo.getStatus()==0){
            termIdDto.setStatus(0);
            termIdDto.setSrcBlackServiceNumber(termIdDto.getSpServiceNumber());
            return new Response(0,"主叫客户状态不正常",termIdDto);
        }
        termIdDto.setPriority(String.valueOf(customerVo.getLevel()));
        //查询客户主叫状态是否正常
        TerminalBlackVo terminalBlackVo=new TerminalBlackVo();
        terminalBlackVo.setCustomerCode(customerVo.getCustomerCode());
        terminalBlackVo.setType("MO");
        List<TerminalBlackVo> terminalBlackVos=terminalBlackMapper.selectTerminalBlackByCustomerCode(terminalBlackVo);
        if(ValidateUtil.isNotEmpty(terminalBlackVos)){
            termIdDto.setStatus(0);
            termIdDto.setSrcBlackServiceNumber(termIdDto.getSpServiceNumber());
            return new Response(0,"客户主叫黑名单",termIdDto);
        }
        termIdDto.setSrcRealServiceNumber(termIdDto.getSpServiceNumber());
        List<String> msisdnTermIds=termIdDto.getMsisdnTermIds();
        List<String> destTermRealIds=new ArrayList<>();
        List<String> destTermBlackIds=new ArrayList<>();
        for (String msisdnTermId:msisdnTermIds){
            TerminalVo terminalVo=new TerminalVo();
            terminalVo.setMsisdn(msisdnTermId);
            //查询接收方号码是否存在
            if(terminalMapper.selectterminalbymsisdn(terminalVo)==null){
                destTermBlackIds.add(msisdnTermId);
                continue;
            }
            //查询是否在黑名单
            TerminalBlackVo terminalBlackVo1=new TerminalBlackVo();
            terminalBlackVo1.setMsisdn(msisdnTermId);
            terminalBlackVo1.setType("MT");
            terminalBlackVo1.setLevel("terminal_sys");
            if(terminalBlackMapper.selectTerminalBlackByMsisdn(terminalBlackVo1)!=null){
                //用户系统级限制
                destTermBlackIds.add(msisdnTermId);
                continue;
            }
            terminalBlackVo1.setLevel("terminal_idt");
            terminalBlackVo1=terminalBlackMapper.selectTerminalBlackByMsisdn(terminalBlackVo1);
            if(terminalBlackVo1!=null){
                CustomerVo customerVo1=new CustomerVo();
                customerVo1.setCustomerCode(terminalBlackVo1.getCustomerCode());
                customerVo1=customerMapper.selectCustomerByCustomerCode(customerVo1);
                if(customerVo.getIndustryList().equals(customerVo1.getIndustryList())){
                    //用户行业级限制
                    destTermBlackIds.add(msisdnTermId);
                    continue;
                }
            }
            destTermRealIds.add(msisdnTermId);
        }
        termIdDto.setDestTermBlackIds(destTermBlackIds);
        termIdDto.setDestTermRealIds(destTermRealIds);
        if(destTermRealIds.size()==0){
            termIdDto.setStatus(0);
            return new Response(0,"被叫号码不存在或在黑名单中",termIdDto);
        }
        termIdDto.setStatus(1);
        return  new Response(0,"成功",termIdDto);
    }


    /**
     * 终端发送客户
     * @param termIdRecDto
     * @return
     */
    public Response selectRecTermIds(TermIdRecDto termIdRecDto) {
        //查询终端号码是否存在
        TerminalVo terminalVo=new TerminalVo();
        terminalVo.setMsisdn(termIdRecDto.getMsisdnTermId());
        if(terminalMapper.selectterminalbymsisdn(terminalVo)==null){
            termIdRecDto.setStatus(0);
            termIdRecDto.setSrcBlackMsisdn(termIdRecDto.getMsisdnTermId());
            return new Response(0,"终端号码不存在",termIdRecDto);
        }
        //查询用户是否在黑名单
        TerminalBlackVo terminalBlackVo=new TerminalBlackVo();
        terminalBlackVo.setMsisdn(termIdRecDto.getMsisdnTermId());
        terminalBlackVo.setType("MO");
        terminalBlackVo.setLevel("terminal_sys");
        if(terminalBlackMapper.selectTerminalBlackByMsisdn(terminalBlackVo)!=null){
            //用户系统级限制
            termIdRecDto.setStatus(0);
            termIdRecDto.setSrcBlackMsisdn(termIdRecDto.getMsisdnTermId());
            return new Response(0,"终端号码用户系统级限制",termIdRecDto);
        }
        //查询客户是否在黑名单
        //根据特服号查询客户
        CustomerVo customerVo=new CustomerVo();
        customerVo.setCustomerCode(termIdRecDto.getCustomerCode());
        customerVo=customerMapper.selectCustomerByCustomerCode(customerVo);
        //判断特服号
        if(customerVo==null){
            termIdRecDto.setStatus(0);
            termIdRecDto.setDestBlackServiceNumber(termIdRecDto.getSpServiceNumber());
            return new Response(0,"客户不存在",termIdRecDto);
        }
        if(customerVo.getStatus()==0){
            termIdRecDto.setStatus(0);
            termIdRecDto.setDestBlackServiceNumber(termIdRecDto.getSpServiceNumber());
            return new Response(0,"客户状态不正常",termIdRecDto);
        }
        //查询客户主叫状态是否正常
        TerminalBlackVo terminalBlackVo1=new TerminalBlackVo();
        terminalBlackVo1.setCustomerCode(customerVo.getCustomerCode());
        terminalBlackVo1.setType("MT");
        List<TerminalBlackVo> terminalBlackVos=terminalBlackMapper.selectTerminalBlackByCustomerCode(terminalBlackVo1);
        if(ValidateUtil.isNotEmpty(terminalBlackVos)){
            termIdRecDto.setStatus(0);
            termIdRecDto.setDestBlackServiceNumber(termIdRecDto.getSpServiceNumber());
            return new Response(0,"客户主叫黑名单",termIdRecDto);
        }

        terminalBlackVo.setLevel("terminal_idt");
        terminalBlackVo=terminalBlackMapper.selectTerminalBlackByMsisdn(terminalBlackVo);
        if(terminalBlackVo!=null){
            //客户行业级限制
            //查询用户绑定的客户行业
            CustomerVo customerVo1=new CustomerVo();
            customerVo1.setCustomerCode(terminalBlackVo.getCustomerCode());
            customerVo1=customerMapper.selectCustomerByCustomerCode(customerVo1);
            //查询客户行业
            if(customerVo.getIndustryList().equals(customerVo1.getIndustryList())){
                //用户行业级限制
                termIdRecDto.setStatus(0);
                termIdRecDto.setSrcBlackMsisdn(termIdRecDto.getMsisdnTermId());
                return new Response(0,"终端号码用户系统级限制",termIdRecDto);
            }
        }
        termIdRecDto.setStatus(1);
        termIdRecDto.setSrcRealMsisdn(termIdRecDto.getMsisdnTermId());
        termIdRecDto.setDestRealServiceNumber(termIdRecDto.getSpServiceNumber());
        return new Response(0,"成功",termIdRecDto);
    }
}
