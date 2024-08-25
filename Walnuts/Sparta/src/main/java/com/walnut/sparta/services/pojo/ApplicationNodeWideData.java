package com.walnut.sparta.services.pojo;

import com.pinecone.hydra.service.tree.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.NodeWideData;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;

/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ApplicationNodeWideData implements NodeWideData {
    private GenericApplicationNode applicationNode;
    private GenericApplicationNodeMeta applicationDescription;
    private GenericNodeCommonData metadata;


    public ApplicationNodeWideData() {
    }

    public ApplicationNodeWideData(GenericApplicationNode applicationNode, GenericApplicationNodeMeta applicationDescription, GenericNodeCommonData metadata) {
        this.applicationNode = applicationNode;
        this.applicationDescription = applicationDescription;
        this.metadata = metadata;
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
    public GenericApplicationNodeMeta getApplicationDescription() {
        return applicationDescription;
    }

    /**
     * 设置
     * @param applicationDescription
     */
    public void setApplicationDescription(GenericApplicationNodeMeta applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    /**
     * 获取
     * @return metadata
     */
    public GenericNodeCommonData getMetadata() {
        return metadata;
    }

    /**
     * 设置
     * @param metadata
     */
    public void setMetadata(GenericNodeCommonData metadata) {
        this.metadata = metadata;
    }

    public String toString() {
        return "ApplicationNodeInformation{applicationNode = " + applicationNode + ", applicationDescription = " + applicationDescription + ", metadata = " + metadata + "}";
    }

    @Override
    public String getName() {
        return this.applicationNode.getName();
    }
}
