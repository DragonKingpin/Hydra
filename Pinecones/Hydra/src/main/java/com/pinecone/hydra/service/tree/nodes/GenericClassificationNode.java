package com.pinecone.hydra.service.tree.nodes;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ClassificationRules;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.NodeCommonData;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;
import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public class GenericClassificationNode implements ClassificationNode {

    private GUIDDistributedScopeNode distributedTreeNode;

    private GenericClassificationRules classificationRules;

    private GenericNodeCommonData nodeCommonData;

    // 节点id
    private long enumId;

    // 节点uuid
    private GUID guid;

    // 节点名称
    private String name;

    // 分类规则uuid
    private GUID rulesGUID;


    public GenericClassificationNode() {
    }

    public GenericClassificationNode(GUIDDistributedScopeNode distributedTreeNode, GenericClassificationRules classificationRules, GenericNodeCommonData nodeCommonData, long enumId, GUID guid, String name, GUID rulesGUID) {
        this.distributedTreeNode = distributedTreeNode;
        this.classificationRules = classificationRules;
        this.nodeCommonData = nodeCommonData;
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
        this.rulesGUID = rulesGUID;
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

    /**
     * 获取
     * @return rulesGUID
     */
    public GUID getRulesGUID() {
        return rulesGUID;
    }

    /**
     * 设置
     * @param rulesGUID
     */
    public void setRulesGUID(GUID rulesGUID) {
        this.rulesGUID = rulesGUID;
    }

    public String toString() {
        return "GenericClassificationNode{distributedTreeNode = " + distributedTreeNode + ", classificationRules = " + classificationRules + ", nodeCommonData = " + nodeCommonData + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", rulesGUID = " + rulesGUID + "}";
    }
}
