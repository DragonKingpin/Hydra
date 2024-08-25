package com.walnut.sparta.services.pojo;

import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ClassificationNodeMeta implements FunctionalNodeMeta {
    private GenericNodeCommonData nodeMetadata;
    private GenericClassificationNode classificationNode;
    private GenericClassificationRules classificationRules;


    public ClassificationNodeMeta() {
    }

    public ClassificationNodeMeta(GenericNodeCommonData nodeMetadata, GenericClassificationNode classificationNode, GenericClassificationRules classificationRules) {
        this.nodeMetadata = nodeMetadata;
        this.classificationNode = classificationNode;
        this.classificationRules = classificationRules;
    }

    /**
     * 获取
     * @return nodeMetadata
     */
    public GenericNodeCommonData getNodeMetadata() {
        return nodeMetadata;
    }

    /**
     * 设置
     * @param nodeMetadata
     */
    public void setNodeMetadata(GenericNodeCommonData nodeMetadata) {
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
        return "ClassifNodeInformation{nodeMetadata = " + nodeMetadata + ", classificationNode = " + classificationNode + ", classificationRules = " + classificationRules + "}";
    }

    @Override
    public String getName() {
        return this.classificationNode.getName();
    }
}
