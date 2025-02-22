package com.pinecone.hydra.deploy.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.entity.iface.Physical;

public class GenericPhysical implements Physical {
    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public GUID getGuid() {
        return null;
    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public String getHardwareSpecs() {
        return null;
    }

    @Override
    public void setHardwareSpecs(String hardwareSpecs) {

    }

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
}
