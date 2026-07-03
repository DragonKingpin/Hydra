package com.walnut.redstone.ether.operation;

import com.pinecone.framework.system.prototype.Pinenut;

public class OperationResult implements Pinenut {
    protected boolean success;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess( boolean success ) {
        this.success = success;
    }
}

