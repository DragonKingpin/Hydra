package com.pinecone.hydra.proc.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcessStatus;

public interface ProcessElement extends ElementNode {

    String getName();

    long getLocalPID();

    default GUID getPID() {
        return this.getGuid();
    }

    @Override
    GUID getGuid();

    GUID getParentProcessId();

    long getParentLocalPID();

    UProcessStatus getStatus();

    ElementNode getAccount();

}
