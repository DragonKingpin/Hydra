package com.pinecone.hydra.device.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceExtraMeta;

public interface VirtualMachineElement extends ServerElement {

    @Override
    default VirtualMachineElement evinceVirtualMachineElement() {
        return this;
    }


/*    String getName();
    void setName(String name);*/

    String getIpAddress();
    void setIpAddress( String ipAddress );

    String getStatus();
    void setStatus( String status );


    GUID getAffiliateHostGuid();
    void setAffiliateHostGuid( GUID guid );

    String getImageName();
    void setImageName( String imageName );

    String getOsName();
    void setOsName( String osName );

    String getCpuArch();
    void setCpuArch( String cpuArch );

    Integer getCpuCores();
    void setCpuCores( Integer cpuCores );

    Long getMemoryMb();
    void setMemoryMb( Long memoryMb );

    Long getStorageGb();
    void setStorageGb( Long storageGb );

    DeviceExtraMeta getVmExtraMeta();
}
