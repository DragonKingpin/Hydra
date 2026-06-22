package com.walnut.odin.conduct.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import java.util.Map;

public class GenericInstanceLineageAdjacent implements InstanceLineageAdjacent {
    protected GUID guid;
    protected GUID parentGuid;

    public GenericInstanceLineageAdjacent() {
    }

    public GenericInstanceLineageAdjacent(Map<String, Object> joEntity) {
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