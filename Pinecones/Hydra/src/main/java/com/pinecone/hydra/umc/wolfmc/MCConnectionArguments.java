package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.framework.system.prototype.Pinenut;

public interface MCConnectionArguments extends Pinenut {
    String getHost();

    void setHost(String host);

    short getPort();

    void setPort( short port );

    int getKeepAliveTimeout();

    void setKeepAliveTimeout( int keepAliveTimeout );

    int getSocketTimeout();

    void setSocketTimeout( int socketTimeout );
}
