package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;

public class HuskyDeviceDeregisterInstruction implements Pinenut {

    protected String instanceGuid;

    protected String reason;

    public String getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( String instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason( String reason ) {
        this.reason = reason;
    }
}
