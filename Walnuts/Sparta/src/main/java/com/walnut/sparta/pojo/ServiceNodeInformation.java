package com.walnut.sparta.pojo;

import com.walnut.sparta.entity.ApplicationNode;
import com.walnut.sparta.entity.Node;
import com.walnut.sparta.entity.NodeMetadata;
import com.walnut.sparta.entity.ServiceDescription;
import com.walnut.sparta.entity.ServiceNode;
/**
 * 用于便捷获取节点数据与生成json数据
 */
public class ServiceNodeInformation {
    private Node node;
    private ServiceNode serviceNode;
    private NodeMetadata nodeMetadata;
    private ServiceDescription serviceDescription;

    public ServiceNodeInformation() {
    }

    public ServiceNodeInformation(Node node, ServiceNode serviceNode, NodeMetadata nodeMetadata, ServiceDescription serviceDescription) {
        this.node = node;
        this.serviceNode = serviceNode;
        this.nodeMetadata = nodeMetadata;
        this.serviceDescription = serviceDescription;
    }

    /**
     * 获取
     * @return node
     */
    public Node getNode() {
        return node;
    }

    /**
     * 设置
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * 获取
     * @return serviceNode
     */
    public ServiceNode getServiceNode() {
        return serviceNode;
    }

    /**
     * 设置
     * @param serviceNode
     */
    public void setServiceNode(ServiceNode serviceNode) {
        this.serviceNode = serviceNode;
    }

    /**
     * 获取
     * @return nodeMetadata
     */
    public NodeMetadata getNodeMetadata() {
        return nodeMetadata;
    }

    /**
     * 设置
     * @param nodeMetadata
     */
    public void setNodeMetadata(NodeMetadata nodeMetadata) {
        this.nodeMetadata = nodeMetadata;
    }

    /**
     * 获取
     * @return serviceDescription
     */
    public ServiceDescription getServiceDescription() {
        return serviceDescription;
    }

    /**
     * 设置
     * @param serviceDescription
     */
    public void setServiceDescription(ServiceDescription serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String toString() {
        return "ServiceNodeInformation{node = " + node + ", serviceNode = " + serviceNode + ", nodeMetadata = " + nodeMetadata + ", serviceDescription = " + serviceDescription + "}";
    }
}
