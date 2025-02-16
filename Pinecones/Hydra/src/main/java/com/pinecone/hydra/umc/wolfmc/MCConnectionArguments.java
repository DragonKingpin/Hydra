package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.MsgNodeConfig;

public interface MCConnectionArguments extends MsgNodeConfig {
    String getHost();

    void setHost( String host );

    short getPort();

    void setPort( short port );

    int getKeepAliveTimeout();

    void setKeepAliveTimeout( int keepAliveTimeout );

    int getSocketTimeout();

    void setSocketTimeout( int socketTimeout );

    boolean isEnableHeartbeat() ;

    void setHeartbeatState( boolean enable ) ;

    long getHeartbeatInterval();

    void setHeartbeatInterval( long heartbeatIntervalMills );

    @Override
    default long getSyncWaitingMillis() {
        return this.getKeepAliveTimeout() * 1000L;
    }
}
