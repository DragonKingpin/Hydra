package com.pinecone.hydra.device.kom.entity;

public interface PhysicalHostElement extends ServerElement {

    void setHardwareSpecs( String hardwareSpecs );
    String getHardwareSpecs();

    void setOsName( String osName );
    String getOsName();

    void setCpuArch( String cpuArch );
    String getCpuArch();

    void setCpuCores( Integer cpuCores );
    Integer getCpuCores();

    void setMemoryMb( Long memoryMb );
    Long getMemoryMb();

    void setStorageGb( Long storageGb );
    Long getStorageGb();

    void setGpuCount( Integer gpuCount );
    Integer getGpuCount();

    void setGpuModel( String gpuModel );
    String getGpuModel();

    void setStatus( String status );
    String getStatus();

    default PhysicalHostElement evincePhysicalHostElement() {
        return this;
    }

}
