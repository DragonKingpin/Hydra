package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericConfNodeMeta implements ConfNodeMeta{
    private int enumId;

    private GUID guid;


    public GenericConfNodeMeta() {
    }

    public GenericConfNodeMeta(int enumId, GUID guid) {
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
        return "GenericConfNodeMeta{enumId = " + enumId + ", guid = " + guid + "}";
    }
}
