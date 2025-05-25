package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.DeployExtraMeta;

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


    DeployExtraMeta getVmExtraMeta();
}