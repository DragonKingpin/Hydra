package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.NodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.meta.ServiceNodeMeta;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public class GenericServiceNode implements ServiceNode {
    private GUIDDistributedScopeNode distributedTreeNode;

    private GenericServiceNodeMeta serviceNodeMetadata;

    private GenericNodeCommonData nodeCommonData;

    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID guid;

    // 服务节点名称
    private String name;


    public GenericServiceNode() {
    }

    public GenericServiceNode(GUIDDistributedScopeNode distributedTreeNode, GenericServiceNodeMeta serviceNodeMetadata, GenericNodeCommonData nodeCommonData, long enumId, GUID guid, String name) {
        this.distributedTreeNode = distributedTreeNode;
        this.serviceNodeMetadata = serviceNodeMetadata;
        this.nodeCommonData = nodeCommonData;
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
    }

    /**
     * 获取
     * @return distributedTreeNode
     */
    public GUIDDistributedScopeNode getDistributedTreeNode() {
        return distributedTreeNode;
    }

    /**
     * 设置
     * @param distributedTreeNode
     */
    public void setDistributedTreeNode(GUIDDistributedScopeNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }

    /**
     * 获取
     * @return serviceNodeMetadata
     */
    public GenericServiceNodeMeta getServiceNodeMetadata() {
        return serviceNodeMetadata;
    }

    /**
     * 设置
     * @param serviceNodeMetadata
     */
    public void setServiceNodeMetadata(GenericServiceNodeMeta serviceNodeMetadata) {
        this.serviceNodeMetadata = serviceNodeMetadata;
    }

    /**
     * 获取
     * @return nodeCommonData
     */
    public GenericNodeCommonData getNodeCommonData() {
        return nodeCommonData;
    }

    /**
     * 设置
     * @param nodeCommonData
     */
    public void setNodeCommonData(GenericNodeCommonData nodeCommonData) {
        this.nodeCommonData = nodeCommonData;
    }

    /**
     * 获取
     * @return enumId
     */
    public long getEnumId() {
        return enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return guid
     */
    public GUID getGuid() {
        return guid;
    }

    /**
     * 设置
     * @param guid
     */
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "GenericServiceNode{distributedTreeNode = " + distributedTreeNode + ", serviceNodeMetadata = " + serviceNodeMetadata + ", nodeCommonData = " + nodeCommonData + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}