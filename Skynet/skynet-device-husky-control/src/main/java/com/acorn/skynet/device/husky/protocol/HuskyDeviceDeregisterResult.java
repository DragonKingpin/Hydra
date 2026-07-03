package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;

public class HuskyDeviceDeregisterResult implements Pinenut {

    protected String instanceGuid;

    public String getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( String instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }
}
