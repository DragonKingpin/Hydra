package com.pinecone.hydra.service.kom.nodes;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.ServicesTree;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericNamespace implements Namespace {

    private GUIDDistributedTrieNode distributedTreeNode;

    private GenericNamespaceRules classificationRules;

    private GenericNodeCommonData nodeAttributes;

    // 节点id
    private long enumId;

    // 节点uuid
    private GUID guid;

    // 节点名称
    private String name;

    // 分类规则uuid
    private GUID rulesGUID;

    private ServicesTree servicesTree;
    private ServiceNamespaceManipulator namespaceManipulator;

    public GenericNamespace() {
    }

    public GenericNamespace(ServicesTree servicesTree ) {
        this.servicesTree = servicesTree;
        GuidAllocator guidAllocator = this.servicesTree.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericNamespace( ServicesTree servicesTree, ServiceNamespaceManipulator namespaceManipulator ) {
        this(servicesTree);
        this.namespaceManipulator = namespaceManipulator;
    }


    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }


    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    public GenericNamespaceRules getClassificationRules() {
        return classificationRules;
    }


    public void setClassificationRules(GenericNamespaceRules classificationRules) {
        this.classificationRules = classificationRules;
    }


    public GenericNodeCommonData getAttributes() {
        return nodeAttributes;
    }


    public void setNodeCommonData(GenericNodeCommonData nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
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
        return "GenericClassificationNode{distributedTreeNode = " + distributedTreeNode + ", classificationRules = " + classificationRules + ", nodeAttributes = " + nodeAttributes + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", rulesGUID = " + rulesGUID + "}";
    }
}
