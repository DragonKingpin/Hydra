package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.NodeCommonData;
import com.pinecone.hydra.service.tree.meta.ApplicationNodeMeta;
import com.pinecone.hydra.service.tree.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public class GenericApplicationNode implements ApplicationNode {

    private GUIDDistributedScopeNode distributedTreeNode;

    private GenericApplicationNodeMeta applicationNodeMeta;

    private GenericNodeCommonData nodeCommonData;
    // 应用节点id
    private long enumId;

    // 应用节点UUID
    private GUID guid;

    // 应用节点名称
    private String name;


    public GenericApplicationNode() {
    }

    public GenericApplicationNode(GUIDDistributedScopeNode distributedTreeNode, GenericApplicationNodeMeta applicationNodeMeta, GenericNodeCommonData nodeCommonData, long enumId, GUID guid, String name) {
        this.distributedTreeNode = distributedTreeNode;
        this.applicationNodeMeta = applicationNodeMeta;
        this.nodeCommonData = nodeCommonData;
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
    }


    public GUIDDistributedScopeNode getDistributedTreeNode() {
        return distributedTreeNode;
    }


    public void setDistributedTreeNode(GUIDDistributedScopeNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    public GenericApplicationNodeMeta getApplicationNodeMeta() {
        return applicationNodeMeta;
    }


    public void setApplicationNodeMeta(GenericApplicationNodeMeta applicationNodeMeta) {
        this.applicationNodeMeta = applicationNodeMeta;
    }


    public GenericNodeCommonData getNodeCommonData() {
        return nodeCommonData;
    }


    public void setNodeCommonData(GenericNodeCommonData nodeCommonData) {
        this.nodeCommonData = nodeCommonData;
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
        return "GenericApplicationNode{distributedTreeNode = " + distributedTreeNode + ", applicationNodeMeta = " + applicationNodeMeta + ", nodeCommonData = " + nodeCommonData + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}