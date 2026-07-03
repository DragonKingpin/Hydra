package com.pinecone.hydra.device.registry.instruction;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceShutdownInstruction implements Pinenut {

    protected GUID instanceGuid;

    protected String reason;

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason( String reason ) {
        this.reason = reason;
    }
}
