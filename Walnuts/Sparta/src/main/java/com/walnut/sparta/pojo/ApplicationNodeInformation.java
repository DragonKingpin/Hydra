package com.walnut.sparta.pojo;

import com.pinecone.hydra.service.GenericApplicationDescription;
import com.pinecone.hydra.service.GenericApplicationNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;

/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ApplicationNodeInformation {
    private GUIDDistributedScopeNode node;
    private GenericApplicationNode applicationNode;
    private GenericApplicationDescription applicationDescription;
    private GenericNodeMetadata metadata;

    public ApplicationNodeInformation() {
    }

    public ApplicationNodeInformation(GUIDDistributedScopeNode node, GenericApplicationNode applicationNode, GenericApplicationDescription applicationDescription, GenericNodeMetadata metadata) {
        this.node = node;
        this.applicationNode = applicationNode;
        this.applicationDescription = applicationDescription;
        this.metadata = metadata;
    }

    /**
     * 获取
     * @return node
     */
    public GUIDDistributedScopeNode getNode() {
        return node;
    }

    /**
     * 设置
     * @param node
     */
    public void setNode(GUIDDistributedScopeNode node) {
        this.node = node;
    }

    /**
     * 获取
     * @return applicationNode
     */
    public GenericApplicationNode getApplicationNode() {
        return applicationNode;
    }

    /**
     * 设置
     * @param applicationNode
     */
    public void setApplicationNode(GenericApplicationNode applicationNode) {
        this.applicationNode = applicationNode;
    }

    /**
     * 获取
     * @return applicationDescription
     */
    public GenericApplicationDescription getApplicationDescription() {
        return applicationDescription;
    }

    /**
     * 设置
     * @param applicationDescription
     */
    public void setApplicationDescription(GenericApplicationDescription applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    /**
     * 获取
     * @return metadata
     */
    public GenericNodeMetadata getMetadata() {
        return metadata;
    }

    /**
     * 设置
     * @param metadata
     */
    public void setMetadata(GenericNodeMetadata metadata) {
        this.metadata = metadata;
    }

    public String toString() {
        return "ApplicationNodeInformation{node = " + node + ", applicationNode = " + applicationNode + ", applicationDescription = " + applicationDescription + ", metadata = " + metadata + "}";
    }
}
