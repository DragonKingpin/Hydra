package com.walnut.odin.conduct.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import java.util.Map;

public class GenericInstanceAtlasNode implements InstanceAtlasNode {
    protected GUID    guid;
    protected GUID    instanceGuid;
    protected String  nodeName;
    protected boolean isIsolated;

    public GenericInstanceAtlasNode() {
    }

    public GenericInstanceAtlasNode(Map<String, Object> joEntity) {
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
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void setInstanceGuid(GUID instanceGuid) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public boolean isIsolated() {
        return this.isIsolated;
    }

    @Override
    public void setIsIsolated(boolean isIsolated) {
        this.isIsolated = isIsolated;
    }
}