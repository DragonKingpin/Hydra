package com.walnut.sparta.pojo;

import com.walnut.sparta.entity.ApplicationDescription;
import com.walnut.sparta.entity.ApplicationNode;
import com.walnut.sparta.entity.Node;
import com.walnut.sparta.entity.NodeMetadata;

/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ApplicationNodeInformation {
    private Node node;
    private ApplicationNode applicationNode;
    private ApplicationDescription applicationDescription;
    private NodeMetadata metadata;

    public ApplicationNodeInformation() {
    }

    public ApplicationNodeInformation(Node node, ApplicationNode applicationNode, ApplicationDescription applicationDescription, NodeMetadata metadata) {
        this.node = node;
        this.applicationNode = applicationNode;
        this.applicationDescription = applicationDescription;
        this.metadata = metadata;
    }

    /**
     * 获取
     * @return node
     */
    public Node getNode() {
        return node;
    }

    /**
     * 设置
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * 获取
     * @return applicationNode
     */
    public ApplicationNode getApplicationNode() {
        return applicationNode;
    }

    /**
     * 设置
     * @param applicationNode
     */
    public void setApplicationNode(ApplicationNode applicationNode) {
        this.applicationNode = applicationNode;
    }

    /**
     * 获取
     * @return applicationDescription
     */
    public ApplicationDescription getApplicationDescription() {
        return applicationDescription;
    }

    /**
     * 设置
     * @param applicationDescription
     */
    public void setApplicationDescription(ApplicationDescription applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    /**
     * 获取
     * @return metadata
     */
    public NodeMetadata getMetadata() {
        return metadata;
    }

    /**
     * 设置
     * @param metadata
     */
    public void setMetadata(NodeMetadata metadata) {
        this.metadata = metadata;
    }

    public String toString() {
        return "ApplicationNodeInformation{node = " + node + ", applicationNode = " + applicationNode + ", applicationDescription = " + applicationDescription + ", metadata = " + metadata + "}";
    }
}
