package com.walnut.sparta.pojo;

import com.walnut.sparta.entity.ClassificationNode;
import com.walnut.sparta.entity.ClassificationRules;
import com.walnut.sparta.entity.Node;
import com.walnut.sparta.entity.NodeMetadata;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ClassifNodeInformation {
    private Node node;
    private NodeMetadata nodeMetadata;
    private ClassificationNode classificationNode;
    private ClassificationRules classificationRules;


    public ClassifNodeInformation() {
    }

    public ClassifNodeInformation(Node node, NodeMetadata nodeMetadata, ClassificationNode classificationNode, ClassificationRules classificationRules) {
        this.node = node;
        this.nodeMetadata = nodeMetadata;
        this.classificationNode = classificationNode;
        this.classificationRules = classificationRules;
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
     * @return nodeMetadata
     */
    public NodeMetadata getNodeMetadata() {
        return nodeMetadata;
    }

    /**
     * 设置
     * @param nodeMetadata
     */
    public void setNodeMetadata(NodeMetadata nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    /**
     * 获取
     * @return classificationNode
     */
    public ClassificationNode getClassificationNode() {
        return classificationNode;
    }

    /**
     * 设置
     * @param classificationNode
     */
    public void setClassificationNode(ClassificationNode classificationNode) {
        this.classificationNode = classificationNode;
    }

    /**
     * 获取
     * @return classificationRules
     */
    public ClassificationRules getClassificationRules() {
        return classificationRules;
    }

    /**
     * 设置
     * @param classificationRules
     */
    public void setClassificationRules(ClassificationRules classificationRules) {
        this.classificationRules = classificationRules;
    }

    public String toString() {
        return "NodeInformation{node = " + node + ", nodeMetadata = " + nodeMetadata + ", classificationNode = " + classificationNode + ", classificationRules = " + classificationRules + "}";
    }
}
