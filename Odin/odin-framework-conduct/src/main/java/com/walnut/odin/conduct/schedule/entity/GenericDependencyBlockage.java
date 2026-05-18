package com.walnut.odin.conduct.schedule.entity;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;

public class GenericDependencyBlockage implements DependencyBlockage {

    protected GUID instanceGuid;
    protected GUID dependentInstanceGuid;

    public GenericDependencyBlockage() {
    }

    public GenericDependencyBlockage( Map<String, Object> joEntity ) {
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public GUID getDependentInstanceGuid() {
        return this.dependentInstanceGuid;
    }

    @Override
    public void setDependentInstanceGuid( GUID dependentInstanceGuid ) {
        this.dependentInstanceGuid = dependentInstanceGuid;
    }

}
