package com.walnut.odin.conduct.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import java.util.Map;

public class GenericInstanceAtlasAdjacent implements InstanceAtlasAdjacent {
    protected GUID guid;
    protected GUID parentGuid;

    public GenericInstanceAtlasAdjacent() {
    }

    public GenericInstanceAtlasAdjacent(Map<String, Object> joEntity) {
        BeanMapDecoder.BasicDecoder.decode(this, joEntity);
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
    public GUID getParentGuid() {
        return this.parentGuid;
    }

    @Override
    public void setParentGuid(GUID parentGuid) {
        this.parentGuid = parentGuid;
    }
}