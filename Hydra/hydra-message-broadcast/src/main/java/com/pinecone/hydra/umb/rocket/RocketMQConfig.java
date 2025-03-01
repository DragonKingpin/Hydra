package com.pinecone.hydra.umb.rocket;

import java.util.Map;

public class RocketMQConfig implements RocketConfig {
    protected String mszNameServerAddr;
    protected String mszGroupName;
    protected int mnMaxMessageSize;
    protected int mnSendMsgTimeout;
    protected int mnRetryTimesWhenSendFailed;

    public RocketMQConfig(Map<String, Object> conf){
        this.mszNameServerAddr = (String) conf.get( "nameServerAddr" );
        this.mszGroupName = (String) conf.get( "groupName" );
        this.mnMaxMessageSize = (int) conf.get( "maxMessageSize" );
        this.mnSendMsgTimeout = (int) conf.get( "sendMsgTimeout" );
        this.mnRetryTimesWhenSendFailed = (int) conf.get("retryTimesWhenSendFailed");
    }
    public RocketMQConfig( String nameServerAddr, String groupName, int maxMessageSize, int sendMsgTimeout, int retryTimesWhenSendFailed ) {
        this.mszNameServerAddr          = nameServerAddr;
        this.mszGroupName               = groupName;
        this.mnMaxMessageSize           = maxMessageSize;
        this.mnSendMsgTimeout           = sendMsgTimeout;
        this.mnRetryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    @Override
    public String getNameServerAddr() {
        return this.mszNameServerAddr;
    }

    @Override
    public String getGroupName() {
        return this.mszGroupName;
    }

    @Override
    public int getMaxMessageSize() {
        return this.mnMaxMessageSize;
    }

    @Override
    public int getSendMsgTimeout() {
        return this.mnSendMsgTimeout;
    }

    @Override
    public int getRetryTimesWhenSendFailed() {
        return this.mnRetryTimesWhenSendFailed;
    }
}
