package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

public class GenericNamespaceNodeMeta implements NamespaceNodeMeta{
    private int enumId;

    private GUID guid;

    public GenericNamespaceNodeMeta() {
    }

    public GenericNamespaceNodeMeta( int enumId, GUID guid ) {
        this.enumId = enumId;
        this.guid = guid;
    }

    @Override
    public int getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
