package com.pinecone.hydra.service.tree.nodes;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.ClassificationRules;
import com.pinecone.hydra.service.tree.GenericClassificationRules;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.unit.udsn.DistributedTreeNode;

public class GenericClassificationNode implements ClassificationNode {

    private DistributedTreeNode distributedTreeNode;

    private ClassificationRules classificationRules;
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

    public GenericClassificationNode(DistributedTreeNode distributedTreeNode, ClassificationRules classificationRules, long enumId, GUID guid, String name, GUID rulesGUID) {
        this.distributedTreeNode = distributedTreeNode;
        this.classificationRules = classificationRules;
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
        this.rulesGUID = rulesGUID;
    }


    public DistributedTreeNode getDistributedTreeNode() {
        return distributedTreeNode;
    }


    public void setDistributedTreeNode(DistributedTreeNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    public ClassificationRules getClassificationRules() {
        return classificationRules;
    }


    public void setClassificationRules(ClassificationRules classificationRules) {
        this.classificationRules = classificationRules;
    }


    public long getEnumId() {
        return enumId;
    }


    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public GUID getRulesGUID() {
        return rulesGUID;
    }


    public void setRulesGUID(GUID rulesGUID) {
        this.rulesGUID = rulesGUID;
    }

    public String toString() {
        return "GenericClassificationNode{distributedTreeNode = " + distributedTreeNode + ", classificationRules = " + classificationRules + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", rulesGUID = " + rulesGUID + "}";
    }
}
