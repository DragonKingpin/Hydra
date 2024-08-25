package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ApplicationNodeMeta;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public class GenericApplicationNode implements ApplicationNode {

    private DistributedTreeNode distributedTreeNode;

    private ApplicationNodeMeta applicationNodeMeta;
    // 应用节点id
    private long enumId;

    // 应用节点UUID
    private GUID guid;

    // 应用节点名称
    private String name;


    public GenericApplicationNode() {
    }

    public GenericApplicationNode(DistributedTreeNode distributedTreeNode, ApplicationNodeMeta applicationNodeMeta, long enumId, GUID guid, String name) {
        this.distributedTreeNode = distributedTreeNode;
        this.applicationNodeMeta = applicationNodeMeta;
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


    public ApplicationNodeMeta getApplicationNodeMeta() {
        return applicationNodeMeta;
    }


    public void setApplicationNodeMeta(ApplicationNodeMeta applicationNodeMeta) {
        this.applicationNodeMeta = applicationNodeMeta;
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
        return "GenericApplicationNode{distributedTreeNode = " + distributedTreeNode + ", applicationNodeMeta = " + applicationNodeMeta + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}