package com.pinecone.hydra.device;

public interface Integration extends Deployment {

    String getStatus();

    void setStatus( String status );
}
