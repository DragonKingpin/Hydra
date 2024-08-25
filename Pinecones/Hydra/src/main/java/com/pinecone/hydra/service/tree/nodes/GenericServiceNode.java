package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ServiceNodeMetadata;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public class GenericServiceNode implements ServiceNode {
    private DistributedTreeNode distributedTreeNode;

    private ServiceNodeMetadata serviceNodeMetadata;

    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID guid;

    // 服务节点名称
    private String name;


    public GenericServiceNode() {
    }

    public GenericServiceNode( long enumId, GUID guid, String name ) {
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
    }


    @Override
    public long getEnumId() {
        return enumId;
    }

    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    @Override
    public GUID getGuid() {
        return guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GenericServiceNode{enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}