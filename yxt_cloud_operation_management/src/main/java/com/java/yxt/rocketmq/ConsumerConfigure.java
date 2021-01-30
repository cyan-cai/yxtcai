package com.java.yxt.rocketmq;

import com.java.yxt.util.ValidateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ConsumerConfigure
 *
 * @author zanglei
 * @version V1.0
 * @description rocketMq消费者配置
 * @Package com.java.yxt.util.rocketmq
 * @date 2020/8/21
 */
@Data
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class ConsumerConfigure {
    /**
     * 组名
     */
    private String groupName;

    private String namesrvAddr;

    private String topics;

    /**
     * 消费者线程数量
     */
    private Integer consumeThreadMin;

    private Integer consumeThreadMax;
    /**
     * 批量消费数量
     */
    private Integer consumeMessageBatchMaxSize;

    /**
     * 是否是集群消费模式，默认是集群模式，true 是集群模式，否则是广播模式
     */
    private String isClusterModel;

    @Autowired(required = false)
    private MessageListenerConcurrently rocketMqPersistenceListenerProcessor;


    @Bean(name = "rocketMqPersistenceConsumer")
    @ConditionalOnProperty(prefix = "rocketmq.consumer",value = "isOnOff",havingValue = "true")
    @ConditionalOnBean(name = "rocketMqPersistenceListenerProcessor")
    public DefaultMQPushConsumer rocketMqPersistenceConsumer(){
        log.info("rocketMq运营管理消费者配置开始初始化！");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);

        if(null!=consumeThreadMin){
            consumer.setConsumeThreadMin(consumeThreadMin);
        }
        if(null!=consumeThreadMax){
            consumer.setConsumeThreadMax(consumeThreadMax);
        }
        if(null!=consumeMessageBatchMaxSize){
            consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        }

        /**
         * 设置监听
          */
        consumer.registerMessageListener(rocketMqPersistenceListenerProcessor);
        /**
         * 默认从最新为获取的位置获取消息
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        /**
         * 设置消费模型 ，默认集群
         */
        consumer.setMessageModel(MessageModel.CLUSTERING);
        String clusterFlag="false";
        if(ValidateUtil.isNotEmpty(isClusterModel) && clusterFlag.equals(isClusterModel)){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }

        try {
            String[] topicStr = topics.split(";");
            for (String tag : topicStr) {
                String[] tagArr = tag.split("~");
                consumer.subscribe(tagArr[0], tagArr[1]);
            }
            consumer.start();
            log.info("运营管理rocketMq消费者配置初始化成功，配置为：{}",consumer.toString());
        } catch (MQClientException e) {
            log.error("初始运营管理rocketMq消费者配置异常{}",e);
        }
        return consumer;
    }

}
