package com.pinecone.hydra.deploy.entity;

import com.pinecone.hydra.deploy.PhysicalHost;
import com.pinecone.hydra.deploy.VirtualMachine;

public class GenericVirtualMachine implements VirtualMachine {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getIpAddress() {
        return null;
    }

    @Override
    public void setIpAddress(String ipAddress) {

    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public PhysicalHost getAffiliateHost() {
        return null;
    }
}
