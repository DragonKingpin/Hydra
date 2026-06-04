package com.pinecone.hydra.service.registry.instruction;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceShutdownInstruction implements Pinenut {

    protected GUID mInstanceGuid;

    protected String mszReason;

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public String getReason() {
        return this.mszReason;
    }

    public void setReason( String szReason ) {
        this.mszReason = szReason;
    }

}
