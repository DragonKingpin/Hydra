package com.pinecone.hydra.service.tree.operator;

import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.meta.GenericServiceNodeMeta;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;

/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ServiceNodeWideData implements ServiceTreeNode {
    private GenericServiceNode            serviceNode;
    private GenericNodeCommonData         nodeMetadata;
    private GenericServiceNodeMeta serviceDescription;


    public ServiceNodeWideData() {
    }

    public ServiceNodeWideData(GenericServiceNode serviceNode, GenericNodeCommonData nodeMetadata, GenericServiceNodeMeta serviceDescription ) {
        this.serviceNode        = serviceNode;
        this.nodeMetadata       = nodeMetadata;
        this.serviceDescription = serviceDescription;
    }


    public GenericServiceNode getServiceNode() {
        return serviceNode;
    }

    public void setServiceNode(GenericServiceNode serviceNode) {
        this.serviceNode = serviceNode;
    }

    public GenericNodeCommonData getNodeMetadata() {
        return nodeMetadata;
    }

    public void setNodeMetadata(GenericNodeCommonData nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    public GenericServiceNodeMeta getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(GenericServiceNodeMeta serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String toString() {
        return "ServiceNodeInformation{serviceNode = " + serviceNode + ", nodeMetadata = " + nodeMetadata + ", serviceDescription = " + serviceDescription + "}";
    }

    @Override
    public String getName() {
        return this.serviceNode.getName();
    }
}