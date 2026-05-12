package com.pinecone.hydra.device.entity;

import com.pinecone.hydra.device.Quick;

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
