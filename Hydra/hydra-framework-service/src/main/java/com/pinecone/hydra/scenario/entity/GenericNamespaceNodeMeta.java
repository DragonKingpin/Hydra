package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericNamespaceNodeMeta implements NamespaceNodeMeta{
    private long enumId;
    private GUID guid;

    public GenericNamespaceNodeMeta() {
    }

    public GenericNamespaceNodeMeta(long enumId, GUID guid) {
        this.enumId = enumId;
        this.guid = guid;
    }


    public long getEnumId() {
        return enumId;
    }


    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    public String toString() {
        return "GenericNamespaceNodeMeta{enumId = " + enumId + ", guid = " + guid + "}";
    }
}
