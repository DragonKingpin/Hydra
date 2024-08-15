package com.walnut.sparta.pojo;

import com.pinecone.hydra.unit.udsn.UUIDDistributedScopeNode;
import com.pinecone.hydra.service.GenericNodeMetadata;
import com.pinecone.hydra.service.GenericServiceDescription;
import com.pinecone.hydra.service.GenericServiceNode;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ServiceNodeInformation {
    private UUIDDistributedScopeNode node;
    private GenericServiceNode serviceNode;
    private GenericNodeMetadata nodeMetadata;
    private GenericServiceDescription serviceDescription;

    public ServiceNodeInformation() {
    }

    public ServiceNodeInformation(UUIDDistributedScopeNode node, GenericServiceNode serviceNode, GenericNodeMetadata nodeMetadata, GenericServiceDescription serviceDescription) {
        this.node = node;
        this.serviceNode = serviceNode;
        this.nodeMetadata = nodeMetadata;
        this.serviceDescription = serviceDescription;
    }

    /**
     * 获取
     * @return node
     */
    public UUIDDistributedScopeNode getNode() {
        return node;
    }

    /**
     * 设置
     * @param node
     */
    public void setNode(UUIDDistributedScopeNode node) {
        this.node = node;
    }

    /**
     * 获取
     * @return serviceNode
     */
    public GenericServiceNode getServiceNode() {
        return serviceNode;
    }

    /**
     * 设置
     * @param serviceNode
     */
    public void setServiceNode(GenericServiceNode serviceNode) {
        this.serviceNode = serviceNode;
    }

    /**
     * 获取
     * @return nodeMetadata
     */
    public GenericNodeMetadata getNodeMetadata() {
        return nodeMetadata;
    }

    /**
     * 设置
     * @param nodeMetadata
     */
    public void setNodeMetadata(GenericNodeMetadata nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    /**
     * 获取
     * @return serviceDescription
     */
    public GenericServiceDescription getServiceDescription() {
        return serviceDescription;
    }

    /**
     * 设置
     * @param serviceDescription
     */
    public void setServiceDescription(GenericServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String toString() {
        return "ServiceNodeInformation{node = " + node + ", serviceNode = " + serviceNode + ", nodeMetadata = " + nodeMetadata + ", serviceDescription = " + serviceDescription + "}";
    }
}
