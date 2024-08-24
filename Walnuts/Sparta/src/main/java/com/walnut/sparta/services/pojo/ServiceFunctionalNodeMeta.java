package com.walnut.sparta.services.pojo;

import com.pinecone.hydra.service.tree.FunctionalNodeMeta;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;
import com.pinecone.hydra.service.tree.GenericServiceNodeMetadata;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ServiceFunctionalNodeMeta implements FunctionalNodeMeta {
    private GenericServiceNode serviceNode;
    private GenericNodeCommonData nodeMetadata;
    private GenericServiceNodeMetadata serviceDescription;


    public ServiceFunctionalNodeMeta() {
    }

    public ServiceFunctionalNodeMeta(GenericServiceNode serviceNode, GenericNodeCommonData nodeMetadata, GenericServiceNodeMetadata serviceDescription) {
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
    public GenericNodeCommonData getNodeMetadata() {
        return nodeMetadata;
    }

    /**
     * 设置
     * @param nodeMetadata
     */
    public void setNodeMetadata(GenericNodeCommonData nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    /**
     * 获取
     * @return serviceDescription
     */
    public GenericServiceNodeMetadata getServiceDescription() {
        return serviceDescription;
    }

    /**
     * 设置
     * @param serviceDescription
     */
    public void setServiceDescription(GenericServiceNodeMetadata serviceDescription) {
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
