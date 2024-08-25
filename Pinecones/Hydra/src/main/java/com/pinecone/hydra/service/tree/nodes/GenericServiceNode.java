package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.meta.ServiceNodeMeta;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public class GenericServiceNode implements ServiceNode {
    private DistributedTreeNode distributedTreeNode;

    private ServiceNodeMeta serviceNodeMetadata;

    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID guid;

    // 服务节点名称
    private String name;


    public GenericServiceNode() {
    }

    public GenericServiceNode(DistributedTreeNode distributedTreeNode, ServiceNodeMeta serviceNodeMetadata, long enumId, GUID guid, String name) {
        this.distributedTreeNode = distributedTreeNode;
        this.serviceNodeMetadata = serviceNodeMetadata;
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
    }


    public DistributedTreeNode getDistributedTreeNode() {
        return distributedTreeNode;
    }


    public void setDistributedTreeNode(DistributedTreeNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    public ServiceNodeMeta getServiceNodeMetadata() {
        return serviceNodeMetadata;
    }


    public void setServiceNodeMetadata(ServiceNodeMeta serviceNodeMetadata) {
        this.serviceNodeMetadata = serviceNodeMetadata;
    }


    public long getEnumId() {
        return enumId;
    }


    public void setEnumId(long enumId) {
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
        return "GenericServiceNode{distributedTreeNode = " + distributedTreeNode + ", serviceNodeMetadata = " + serviceNodeMetadata + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}