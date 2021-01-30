package com.java.yxt.enums;

public enum MessageTopicAndTagEnum {

    /**
     * 【短数据】上行数据：计费标识请求
     */
    SD_CHARGING_REQ("计费标识请求","SD_TOPIC","CHARGING_REQ_TAG"),
    /**
     * 【短数据】上行数据：计费标识确认
     */
    SD_CHARGING_CONFIRM("计费标识确认","SD_TOPIC","CHARGING_CONFIRM_TAG"),
    /**
     * 【短数据】上行数据：终端停复机通知
     */
    SD_CHARGE_NOTIFY("终端停复机通知","SD_TOPIC","CHARGE_NOTIFY_TAG")
    ;
    /**
     * 消息topic
     */
    private String topic;
    /**
     * 消息tag
     */
    private String tag;
    /**
     * 消息描述
     */
    private String des;

    MessageTopicAndTagEnum(String des, String topic, String tag){
        this.des = des;
        this.topic = topic;
        this.tag = tag;
    }

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public String getDes() {
        return des;
    }


    public static MessageTopicAndTagEnum getMessageTopicAndTagEnum(String topic, String tag){
        for(MessageTopicAndTagEnum a:values()){
            if(a.getTopic().equals(topic) && a.getTag().equals(tag)){
                return a;
            }
        }
        return null;
    }
}
