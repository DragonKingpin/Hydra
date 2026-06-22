package com.walnut.odin.conduct.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import java.util.Map;

public class GenericInstanceLineageNode implements InstanceLineageNode {
    protected GUID    guid;
    protected GUID    instanceGuid;
    protected String  nodeName;
    protected boolean source;

    public GenericInstanceLineageNode() {
    }

    public GenericInstanceLineageNode(Map<String, Object> joEntity) {
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
    public boolean isSource() {
        return this.source;
    }

    @Override
    public void setSource( boolean source ) {
        this.source = source;
    }
}
