package com.java.yxt.rocketmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.enums.MessageTopicAndTagEnum;
import com.java.yxt.service.HanlderMqMessageService;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author zanglei
 * @version V1.0
 * @description 消息处理
 * @Package com.java.yxt.rocketmq
 * @date 2020/12/30
 */
@Component
@Slf4j
public class HanlderMQMessage {

    @Autowired
    HanlderMqMessageService hanlderMqMessageService;


    public void handler(MessageExt messageExt) throws UnsupportedEncodingException, JsonProcessingException {
        String topic = messageExt.getTopic();
        String tag = messageExt.getTags();
        String body = new String(messageExt.getBody(),"utf-8");
        log.info("接收的mq消息，topic:{},tag:{},body:{}",topic,tag,body);
        if(ValidateUtil.isEmpty(body)){
           return;
        }

        Map<String, Object> map = JsonUtil.json2Map(body);
        MessageTopicAndTagEnum messageTopicAndTagEnum = MessageTopicAndTagEnum.getMessageTopicAndTagEnum(topic,tag);

        switch (messageTopicAndTagEnum){
            case SD_CHARGING_REQ:
                // 计费标识请求
                hanlderMqMessageService.shortdataChargingReq(map);
                 break;
            case SD_CHARGING_CONFIRM:
                // TODO 遥闭确认消息发往外部系统
               // break;
            case SD_CHARGE_NOTIFY:
                // 终端停复机通知
                hanlderMqMessageService.shortDataChargeNotify(map);
            default:

        }
    }

}
