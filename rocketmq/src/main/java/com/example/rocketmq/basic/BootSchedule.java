package com.example.rocketmq.basic;


import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class BootSchedule {
    private static final Logger logger = LoggerFactory.getLogger(BootSchedule.class);
    @Autowired
    DefaultMQProducer defaultMQProducer;


    @Scheduled(fixedRate = 15000)
    public void schedule01() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
            String msg="黄钰滢小仙女";
            logger.info("开始发送消息"+msg);
            defaultMQProducer.send(new Message("topic2","mytag",msg.getBytes()));
    }

}