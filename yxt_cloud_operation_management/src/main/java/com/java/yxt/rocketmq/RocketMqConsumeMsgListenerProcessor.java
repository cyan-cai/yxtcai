package com.java.yxt.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RocketMqConsumeMsgListenerProcessor
 *
 * @author zanglei
 * @version V1.0
 * @description rocketMQ消费者监听
 * @Package com.java.yxt.rocketmq
 * @date 2020/8/21
 */
@Component("rocketMqPersistenceListenerProcessor")
@Slf4j
public class RocketMqConsumeMsgListenerProcessor implements MessageListenerConcurrently {

    @Autowired
    HanlderMQMessage hanlderMQMessage;

    /**
     * 默认msg里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
     * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     * @param msgList
     * @param consumeConcurrentlyContext
     * @return
     */
    @Override
    public  ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if(CollectionUtils.isEmpty(msgList)){
            log.info("MQ接收消息为空，直接返回成功！");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }

        MessageExt messageExt = msgList.get(0);

        try {
            hanlderMQMessage.handler(messageExt);
        } catch (Exception e) {
            log.error("处理MQ消息异常，消息体：{}\n异常信息:",messageExt.toString(),e);
//            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
