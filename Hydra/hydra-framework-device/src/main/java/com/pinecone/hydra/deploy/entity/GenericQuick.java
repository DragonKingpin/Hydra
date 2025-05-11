package com.pinecone.hydra.deploy.entity;

import com.pinecone.hydra.deploy.Quick;

public class GenericQuick implements Quick {

    protected String status;

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
          this.status = status;
    }
}
