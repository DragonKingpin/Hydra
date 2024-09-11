package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericNodeCommonData implements NodeCommonData{
    private int enumId;

    private GUID guid;

    public GenericNodeCommonData() {
    }

    public GenericNodeCommonData(int enumId, GUID guid) {
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
        return "GenericNodeCommonData{enumId = " + enumId + ", guid = " + guid + "}";
    }
}
