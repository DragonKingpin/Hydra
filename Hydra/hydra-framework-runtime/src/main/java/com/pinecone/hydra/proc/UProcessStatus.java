package com.pinecone.hydra.proc;

import com.pinecone.framework.system.prototype.Pinenut;

public enum UProcessStatus implements Pinenut {
    Registered,
    Preparing,
    Created,
    Activated,
    Running,
    Suspended,
    Terminated,
    Error,
    Unknown;

    public boolean isTerminal() {
        return this == Terminated || this == Error;
    }

    public String getName() {
        return this.name();
    }

    @Override
    public String toJSONString() {
        return "\"" + this.name() + "\"";
    }
}
