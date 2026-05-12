package com.pinecone.hydra.device;

public interface Server extends Deployment {

    String getName();

    void setName( String name );

    String getIpAddress();

    void setIpAddress( String ipAddress );

    String getStatus();

    void setStatus( String status );
}
