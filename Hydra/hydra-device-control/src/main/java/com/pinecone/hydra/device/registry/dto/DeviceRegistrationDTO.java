package com.pinecone.hydra.device.registry.dto;

public class DeviceRegistrationDTO implements DeviceDTO {

    protected String guid;

    protected String path;

    protected DeviceMetaDTO meta;

    public String getGuid() {
        return this.guid;
    }

    public void setGuid( String guid ) {
        this.guid = guid;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public DeviceMetaDTO getMeta() {
        return this.meta;
    }

    public void setMeta( DeviceMetaDTO meta ) {
        this.meta = meta;
    }
}
