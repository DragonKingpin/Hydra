package com.pinecone.hydra.service.kom.nodes;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.BaseNodeCommonData;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.hydra.unit.udtt.GUIDDistributedTrieNode;
import com.pinecone.ulf.util.id.GuidAllocator;

public class GenericServiceNode extends BaseNodeCommonData implements ServiceNode {
    private GUIDDistributedTrieNode distributedTreeNode;

    private GenericServiceNodeMeta serviceNodeMetadata;

    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID guid;

    // 服务节点名称
    private String name;
    private ServicesInstrument servicesInstrument;
    private ServiceNodeManipulator serviceNodeManipulator;


    public GenericServiceNode() {
    }

    public GenericServiceNode(ServicesInstrument servicesInstrument) {
        this.servicesInstrument = servicesInstrument;
        GuidAllocator guidAllocator = this.servicesInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID72() );
    }

    public GenericServiceNode(ServicesInstrument servicesInstrument, ServiceNodeManipulator serviceNodeManipulator ) {
        this(servicesInstrument);
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
        return "GenericServiceNode{distributedTreeNode = " + distributedTreeNode + ", serviceNodeMetadata = " + serviceNodeMetadata + ", enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}