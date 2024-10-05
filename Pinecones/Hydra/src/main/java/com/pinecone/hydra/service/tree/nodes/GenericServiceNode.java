package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;

public class GenericServiceNode implements ServiceNode {
    private GUIDDistributedTrieNode distributedTreeNode;

    private GenericServiceNodeMeta serviceNodeMetadata;

    private GenericNodeCommonData nodeAttributes;

    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID guid;

    // 服务节点名称
    private String name;


    public GenericServiceNode() {
    }

    public GenericServiceNode(GUIDDistributedTrieNode distributedTreeNode, GenericServiceNodeMeta serviceNodeMetadata, GenericNodeCommonData nodeAttributes, long enumId, GUID guid, String name) {
        this.distributedTreeNode = distributedTreeNode;
        this.serviceNodeMetadata = serviceNodeMetadata;
        this.nodeAttributes = nodeAttributes;
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
    }

    /**
     * 获取
     * @return distributedTreeNode
     */
    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }

    /**
     * 设置
     * @param distributedTreeNode
     */
    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
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
     * @return nodeAttributes
     */
    public GenericNodeCommonData getAttributes() {
        return nodeAttributes;
    }

    /**
     * 设置
     * @param nodeAttributes
     */
    public void setNodeCommonData(GenericNodeCommonData nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
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
        return "GenericServiceNode{distributedTreeNode = " + distributedTreeNode + ", serviceNodeMetadata = " + serviceNodeMetadata + ", nodeAttributes = " + nodeAttributes + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}