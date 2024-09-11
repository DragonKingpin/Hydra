package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.util.id.GUID;

public class GenericNamespaceNode implements NamespaceNode{
    private int enumId;
    private GUID guid;
    private String name;

    public GenericNamespaceNode() {
    }

    public GenericNamespaceNode(int enumId, GUID guid, String name) {
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "GenericNamespaceNode{enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}
