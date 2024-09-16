package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

public class GenericConfigNodeMeta implements ConfigNodeMeta {
    private int enumId;

    private GUID guid;


    public GenericConfigNodeMeta() {
    }

    public GenericConfigNodeMeta(int enumId, GUID guid) {
        this.enumId = enumId;
        this.guid = guid;
    }

    @Override
    public int getEnumId() {
        return enumId;
    }

    @Override
    public void setEnumId( int enumId ) {
        this.enumId = enumId;
    }

    @Override
    public GUID getGuid() {
        return guid;
    }

    @Override
    public void setGuid( GUID guid ) {
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
