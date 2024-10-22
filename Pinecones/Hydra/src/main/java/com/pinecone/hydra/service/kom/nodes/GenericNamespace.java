package com.pinecone.hydra.service.kom.nodes;


import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.BaseNodeCommonData;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericNamespace implements Namespace {

    private GUIDDistributedTrieNode distributedTreeNode;

    private GenericNamespaceRules classificationRules;

    private BaseNodeCommonData nodeAttributes;

    // 节点id
    private long enumId;

    // 节点uuid
    private GUID guid;

    // 节点名称
    private String name;

    // 分类规则uuid
    private GUID rulesGUID;
    private ServicesInstrument servicesInstrument;
    private ServiceNamespaceManipulator namespaceManipulator;


    public GenericNamespace() {
    }

    public GenericNamespace(ServicesInstrument servicesInstrument) {
        this.servicesInstrument = servicesInstrument;
        GuidAllocator guidAllocator = this.servicesInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericNamespace(ServicesInstrument servicesInstrument, ServiceNamespaceManipulator namespaceManipulator ) {
        this(servicesInstrument);
        this.namespaceManipulator = namespaceManipulator;
    }

    /**
     * 获取
     * @return distributedTreeNode
     */
    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }

    /**
     * 设置
     * @param distributedTreeNode
     */
    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }

    /**
     * 获取
     * @return classificationRules
     */
    public GenericNamespaceRules getClassificationRules() {
        return classificationRules;
    }

    /**
     * 设置
     * @param classificationRules
     */
    public void setClassificationRules(GenericNamespaceRules classificationRules) {
        this.classificationRules = classificationRules;
    }

    /**
     * 获取
     * @return nodeAttributes
     */
    public BaseNodeCommonData getAttributes() {
        return nodeAttributes;
    }

    /**
     * 设置
     * @param nodeAttributes
     */
    public void setNodeCommonData(BaseNodeCommonData nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
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
        return "GenericClassificationNode{distributedTreeNode = " + distributedTreeNode + ", classificationRules = " + classificationRules + ", nodeAttributes = " + nodeAttributes + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", rulesGUID = " + rulesGUID + "}";
    }
}
