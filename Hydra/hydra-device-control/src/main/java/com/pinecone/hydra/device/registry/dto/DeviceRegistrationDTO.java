package com.pinecone.hydra.device.registry.dto;

import com.pinecone.hydra.device.registry.constant.DeviceNodeType;

public class DeviceRegistrationDTO {

    protected String path;

    protected DeviceNodeType nodeType;

    protected DeviceMetaDTO meta;

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public DeviceNodeType getNodeType() {
        return this.nodeType;
    }

    public void setNodeType( DeviceNodeType nodeType ) {
        this.nodeType = nodeType;
    }

    public DeviceMetaDTO getMeta() {
        return this.meta;
    }

    public void setMeta( DeviceMetaDTO meta ) {
        this.meta = meta;
    }
}
