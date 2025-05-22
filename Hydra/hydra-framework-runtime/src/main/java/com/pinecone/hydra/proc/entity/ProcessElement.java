package com.pinecone.hydra.proc.entity;

import com.pinecone.framework.util.id.GUID;

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

    Thread.State getState();

    ElementNode getAccount();

}
