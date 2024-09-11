package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericNamespaceNodeMeta implements NamespaceNodeMeta{
    private int enumId;
    private GUID guid;

    public GenericNamespaceNodeMeta() {
    }

    public GenericNamespaceNodeMeta(int enumId, GUID guid) {
        this.enumId = enumId;
        this.guid = guid;
    }


    public int getEnumId() {
        return enumId;
    }


    public void setEnumId(int enumId) {
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
