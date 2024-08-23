package com.walnut.sparta.services.pojo;

import com.pinecone.hydra.service.tree.FunctionalNodeInformation;
import com.pinecone.hydra.service.tree.GenericNodeMetadata;
import com.pinecone.hydra.service.tree.GenericServiceDescription;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ServiceFunctionalNodeInformation implements FunctionalNodeInformation {
    private GenericServiceNode serviceNode;
    private GenericNodeMetadata nodeMetadata;
    private GenericServiceDescription serviceDescription;


    public ServiceFunctionalNodeInformation() {
    }

    public ServiceFunctionalNodeInformation(GenericServiceNode serviceNode, GenericNodeMetadata nodeMetadata, GenericServiceDescription serviceDescription) {
        this.serviceNode = serviceNode;
        this.nodeMetadata = nodeMetadata;
        this.serviceDescription = serviceDescription;
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
        return "ServiceNodeInformation{serviceNode = " + serviceNode + ", nodeMetadata = " + nodeMetadata + ", serviceDescription = " + serviceDescription + "}";
    }
}
