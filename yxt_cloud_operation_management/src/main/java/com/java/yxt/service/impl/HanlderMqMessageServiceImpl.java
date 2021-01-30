package com.java.yxt.service.impl;

import com.java.yxt.dto.ChargeConfirmDto;
import com.java.yxt.dto.ShortDataChargingStatusResponseDto;
import com.java.yxt.enums.ServerEnum;
import com.java.yxt.feign.ProtocolFeignService;
import com.java.yxt.service.HanlderMqMessageService;
import com.java.yxt.service.TerminalService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.RandomUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.TerminalVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zanglei
 * @version V1.0
 * @description 处理mq消息处理
 * @Package com.java.yxt.service.impl
 * @date 2021/1/5
 */
@Service
@Slf4j
public class HanlderMqMessageServiceImpl implements HanlderMqMessageService {

    @Autowired
    TerminalService terminalService;

    @Autowired
    ProtocolFeignService protocolFeignService;

    /**
     * 计费标识请求处理
     * @param map
     */
    @Override
    public void shortdataChargingReq(Map<String, Object> map) {
        if(ValidateUtil.isEmpty(map)){
            log.warn("Map数据为空：{}",map);
            return;
        }
        ShortDataChargingStatusResponseDto shortDataChargingStatusResponseDto = new ShortDataChargingStatusResponseDto();

        Integer messageSn = Integer.valueOf(map.get("messageSn").toString());
        String msisdn = map.get("msisdn").toString();

        shortDataChargingStatusResponseDto.setMessageSn(messageSn);
        shortDataChargingStatusResponseDto.setMsisdn(msisdn);
        shortDataChargingStatusResponseDto.setServerId(ServerEnum.SP_SERVER.getServerId().shortValue());
        String dateTime = DateTimeUtil.getBasicDateTime();
        String randomNumber = RandomUtil.number(5);
        String requestId = dateTime + randomNumber;
        shortDataChargingStatusResponseDto.setRequestId(requestId);
        shortDataChargingStatusResponseDto.setResult(1);

        TerminalVo terminalVo = new TerminalVo();
        terminalVo.setMsisdn(msisdn);
        List<TerminalVo> terminalVoList = terminalService.selectAll(terminalVo);
        if(ValidateUtil.isNotEmpty(terminalVoList)){
            TerminalVo terminalVo1 = terminalVoList.get(0);
            shortDataChargingStatusResponseDto.setResult(0);
            shortDataChargingStatusResponseDto.setChargeIdentity(terminalVo1.getCharge().byteValue());

            protocolFeignService.chargingRes(shortDataChargingStatusResponseDto);
        }


    }

    /**
     * 停复机通知处理
     * @param map
     */
    @Override
    public void shortDataChargeNotify(Map<String, Object> map) {
        if(ValidateUtil.isEmpty(map)){
            log.warn("Map数据为空：{}",map);
            return;
        }
        ChargeConfirmDto chargeConfirmDto = new ChargeConfirmDto();

        Integer messageSn = Integer.valueOf(map.get("messageSn").toString());
        String msisdn = map.get("msisdn").toString();
        // 停复机状态，0-停机，1-复机
        Integer chargeStatus = Integer.valueOf(map.get("chargeStatus").toString());

        TerminalVo terminalVo = new TerminalVo();
        terminalVo.setMsisdn(msisdn);
        if(chargeStatus.intValue()==0){
            // 3停机保号，4其他停机
            terminalVo.setUserStatus(4);
        }else{
            // 1复机
            terminalVo.setUserStatus(1);
        }
        int count= terminalService.updateBymsisdn(terminalVo);
        chargeConfirmDto.setResult(0);
        if(count == 0){
            log.error("更新终端停复机失败，入参：{}",terminalVo.toString());
            chargeConfirmDto.setResult(2);
        }

        chargeConfirmDto.setMessageSn(messageSn);
        chargeConfirmDto.setMsisdn(msisdn);
        chargeConfirmDto.setServerId(ServerEnum.SP_SERVER.getServerId().shortValue());

        String dateTime = DateTimeUtil.getBasicDateTime();
        String randomNumber = RandomUtil.number(5);
        String requestId = dateTime + randomNumber;

        chargeConfirmDto.setRequestId(Long.parseLong(requestId));
        // 停复机确认调用
        protocolFeignService.chargeConfirm(chargeConfirmDto);
    }
}
