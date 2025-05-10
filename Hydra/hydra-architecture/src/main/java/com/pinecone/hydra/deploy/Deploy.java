package com.pinecone.hydra.deploy;

import com.pinecone.hydra.device.Deployment;

public interface Deploy extends Deployment {
    String getStatus();

    void setStatus(String status);
}
