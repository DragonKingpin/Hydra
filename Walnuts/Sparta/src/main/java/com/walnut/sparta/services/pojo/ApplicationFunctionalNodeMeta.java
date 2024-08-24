package com.walnut.sparta.services.pojo;

import com.pinecone.hydra.service.tree.GenericApplicationNodeMetadata;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;

/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ApplicationFunctionalNodeMeta implements FunctionalNodeMeta {
    private GenericApplicationNode applicationNode;
    private GenericApplicationNodeMetadata applicationDescription;
    private GenericNodeCommonData metadata;


    public ApplicationFunctionalNodeMeta() {
    }

    public ApplicationFunctionalNodeMeta(GenericApplicationNode applicationNode, GenericApplicationNodeMetadata applicationDescription, GenericNodeCommonData metadata) {
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
    public GenericApplicationNodeMetadata getApplicationDescription() {
        return applicationDescription;
    }

    /**
     * 设置
     * @param applicationDescription
     */
    public void setApplicationDescription(GenericApplicationNodeMetadata applicationDescription) {
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
