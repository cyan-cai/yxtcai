package com.java.yxt.rocketmq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ProducerConfigure
 *
 * @author zanglei
 * @version V1.0
 * @description rocketMQ生产者配置
 * @Package com.java.yxt.util.rocketmq
 * @date 2020/8/21
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq.producer")
@Slf4j
public class ProducerConfigure {

    /**
     组名
     */
    private String groupName;
    /**
     *server地址
     */
    private String namesrvAddr;
    /**
     * 消息最大值
      */
    private Integer maxMessageSize;
    /**
     * 消息发送超时时间
     */
    private Integer sendMsgTimeOut;
    /**
     * 失败重试次数
     */
    private Integer retryTimesWhenSendFailed;


    @Bean(name="rocketMqProducer")
    @ConditionalOnProperty(prefix = "rocketmq.producer",value = "isOnOff",havingValue = "true")
    public DefaultMQProducer rocketMqProducer() throws MQClientException {
       log.info("开始创建运营管理rocketMQ生产者！");

        DefaultMQProducer producer = new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(namesrvAddr);
        if(null!=maxMessageSize){
            producer.setMaxMessageSize(maxMessageSize);
        }
        if(null!=sendMsgTimeOut){
            producer.setSendMsgTimeout(maxMessageSize);
        }
        if(null!=retryTimesWhenSendFailed){
            producer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendFailed);
        }
        producer.start();
        log.info("运营管理rocketmq生产者启动成功，配置信息：{} " ,producer.toString());
        return producer;
    }

}
