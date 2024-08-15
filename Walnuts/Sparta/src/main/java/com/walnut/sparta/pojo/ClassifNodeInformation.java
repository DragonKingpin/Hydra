package com.walnut.sparta.pojo;

import com.pinecone.hydra.service.GenericClassificationNode;
import com.pinecone.hydra.service.GenericClassificationRules;
import com.pinecone.hydra.unit.udsn.UUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ClassifNodeInformation {
    private UUIDDistributedScopeNode node;
    private GenericNodeMetadata nodeMetadata;
    private GenericClassificationNode classificationNode;
    private GenericClassificationRules classificationRules;


    public ClassifNodeInformation() {
    }

    public ClassifNodeInformation(UUIDDistributedScopeNode node, GenericNodeMetadata nodeMetadata, GenericClassificationNode classificationNode, GenericClassificationRules classificationRules) {
        this.node = node;
        this.nodeMetadata = nodeMetadata;
        this.classificationNode = classificationNode;
        this.classificationRules = classificationRules;
    }

    /**
     * 获取
     * @return node
     */
    public UUIDDistributedScopeNode getNode() {
        return node;
    }

    /**
     * 设置
     * @param node
     */
    public void setNode(UUIDDistributedScopeNode node) {
        this.node = node;
    }

    /**
     * 获取
     * @return nodeMetadata
     */
    public GenericNodeMetadata getNodeMetadata() {
        return nodeMetadata;
    }

    /**
     * 设置
     * @param nodeMetadata
     */
    public void setNodeMetadata(GenericNodeMetadata nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    /**
     * 获取
     * @return classificationNode
     */
    public GenericClassificationNode getClassificationNode() {
        return classificationNode;
    }

    /**
     * 设置
     * @param classificationNode
     */
    public void setClassificationNode(GenericClassificationNode classificationNode) {
        this.classificationNode = classificationNode;
    }

    /**
     * 获取
     * @return classificationRules
     */
    public GenericClassificationRules getClassificationRules() {
        return classificationRules;
    }

    /**
     * 设置
     * @param classificationRules
     */
    public void setClassificationRules(GenericClassificationRules classificationRules) {
        this.classificationRules = classificationRules;
    }

    public String toString() {
        return "NodeInformation{node = " + node + ", nodeMetadata = " + nodeMetadata + ", classificationNode = " + classificationNode + ", classificationRules = " + classificationRules + "}";
    }
}
