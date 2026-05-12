package com.pinecone.hydra.device.control.dto;

public class DeviceTopologyDTO {

    protected String parentGuid;

    protected String childGuid;

    public DeviceTopologyDTO() {
    }

    public DeviceTopologyDTO( String parentGuid, String childGuid ) {
        this.parentGuid = parentGuid;
        this.childGuid = childGuid;
    }

    public String getParentGuid() {
        return this.parentGuid;
    }

    public void setParentGuid( String parentGuid ) {
        this.parentGuid = parentGuid;
    }

    public String getChildGuid() {
        return this.childGuid;
    }

    public void setChildGuid( String childGuid ) {
        this.childGuid = childGuid;
    }
}
