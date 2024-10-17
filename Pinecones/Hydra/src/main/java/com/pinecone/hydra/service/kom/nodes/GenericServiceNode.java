package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;
import com.pinecone.hydra.service.kom.ServicesTree;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericServiceNode implements ServiceNode {
    private GUIDDistributedTrieNode distributedTreeNode;

    private GenericServiceNodeMeta serviceNodeMetadata;

    private GenericNodeCommonData nodeAttributes;

    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID guid;

    // 服务节点名称
    private String name;
    private ServicesTree servicesTree;
    private ServiceNodeManipulator serviceNodeManipulator;


    public GenericServiceNode() {
    }

    public GenericServiceNode(ServicesTree servicesTree ) {
        this.servicesTree = servicesTree;
        GuidAllocator guidAllocator = this.servicesTree.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericServiceNode( ServicesTree servicesTree, ServiceNodeManipulator serviceNodeManipulator ) {
        this(servicesTree);
        this.serviceNodeManipulator = serviceNodeManipulator;
    }


    public GUIDDistributedTrieNode getDistributedTreeNode() {
        return distributedTreeNode;
    }


    public void setDistributedTreeNode(GUIDDistributedTrieNode distributedTreeNode) {
        this.distributedTreeNode = distributedTreeNode;
    }


    public GenericServiceNodeMeta getServiceNodeMetadata() {
        return serviceNodeMetadata;
    }


    public void setServiceNodeMetadata(GenericServiceNodeMeta serviceNodeMetadata) {
        this.serviceNodeMetadata = serviceNodeMetadata;
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

    public String toString() {
        return "GenericServiceNode{distributedTreeNode = " + distributedTreeNode + ", serviceNodeMetadata = " + serviceNodeMetadata + ", nodeAttributes = " + nodeAttributes + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}