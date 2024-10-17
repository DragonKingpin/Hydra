package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.ServicesTree;
import com.pinecone.hydra.service.kom.meta.GenericApplicationNodeMeta;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericApplicationNode implements ApplicationNode {

    private GUIDDistributedTrieNode distributedTreeNode;

    private GenericApplicationNodeMeta applicationNodeMeta;

    private GenericNodeCommonData nodeAttributes;
    // 应用节点id
    private long enumId;

    // 应用节点UUID
    private GUID guid;

    // 应用节点名称
    private String name;

    private ServicesTree servicesTree;
    private ApplicationNodeManipulator applicationNodeManipulator;


    public GenericApplicationNode() {
    }

    public GenericApplicationNode(ServicesTree servicesTree ) {
       this.servicesTree = servicesTree;
        GuidAllocator guidAllocator = this.servicesTree.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericApplicationNode( ServicesTree servicesTree, ApplicationNodeManipulator applicationNodeManipulator ) {
        this(servicesTree);
        this.applicationNodeManipulator = applicationNodeManipulator;
    }


    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }


    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    public GenericApplicationNodeMeta getApplicationNodeMeta() {
        return applicationNodeMeta;
    }


    public void setApplicationNodeMeta(GenericApplicationNodeMeta applicationNodeMeta) {
        this.applicationNodeMeta = applicationNodeMeta;
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

    @Override
    public ApplicationNode evinceTreeNode() {
        return ApplicationNode.super.evinceTreeNode();
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

    public String toString() {
        return "GenericApplicationNode{distributedTreeNode = " + distributedTreeNode + ", applicationNodeMeta = " + applicationNodeMeta + ", nodeAttributes = " + nodeAttributes + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}