package com.java.yxt.enums;

/**
 * 服务系统
 */
public enum ServerEnum {
    /**
     *
     */
    SP_SERVER(Long.valueOf("10002") ,"SP客户系统");
    /**
     * 系统id
     */
    private Long serverId;
    /**
     * 系统名称
     */
    private String serverName;

    ServerEnum(Long serverId, String serverName){
        this.serverId = serverId;
        this.serverName = serverName;
    }

    public Long getServerId() {
        return serverId;
    }

    public String getServerName() {
        return serverName;
    }
}
