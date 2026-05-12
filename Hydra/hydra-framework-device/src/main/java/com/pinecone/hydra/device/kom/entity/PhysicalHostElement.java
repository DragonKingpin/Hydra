package com.pinecone.hydra.device.kom.entity;

public interface PhysicalHostElement extends ServerElement {

    void setHardwareSpecs( String hardwareSpecs );
    String getHardwareSpecs();

    void setOsName( String osName );
    String getOsName();

    void setCpuArch( String cpuArch );
    String getCpuArch();

    void setStatus( String status );
    String getStatus();

    default PhysicalHostElement evincePhysicalHostElement() {
        return this;
    }

}
