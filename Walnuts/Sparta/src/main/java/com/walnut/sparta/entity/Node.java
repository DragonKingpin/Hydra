package com.walnut.sparta.entity;

import java.util.UUID;

public class Node {
    //节点id
    private String id;
    //节点uuid
    private String UUID;
    //父节点uuid
    private String parentUUID;
    //基础信息uuid
    private String baseDataUUID;
    //元信息uuid
    private String nodeMetadataUUID;
    //节点的类型方便获取数据
    private String type;


    public Node() {
    }

    public Node(String id, String UUID, String parentUUID, String baseDataUUID, String nodeMetadataUUID, String type) {
        this.id = id;
        this.UUID = UUID;
        this.parentUUID = parentUUID;
        this.baseDataUUID = baseDataUUID;
        this.nodeMetadataUUID = nodeMetadataUUID;
        this.type = type;
    }

    /**
     * 获取
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取
     * @return UUID
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * 设置
     * @param UUID
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * 获取
     * @return parentUUID
     */
    public String getParentUUID() {
        return parentUUID;
    }

    /**
     * 设置
     * @param parentUUID
     */
    public void setParentUUID(String parentUUID) {
        this.parentUUID = parentUUID;
    }

    /**
     * 获取
     * @return baseDataUUID
     */
    public String getBaseDataUUID() {
        return baseDataUUID;
    }

    /**
     * 设置
     * @param baseDataUUID
     */
    public void setBaseDataUUID(String baseDataUUID) {
        this.baseDataUUID = baseDataUUID;
    }

    /**
     * 获取
     * @return nodeMetadataUUID
     */
    public String getNodeMetadataUUID() {
        return nodeMetadataUUID;
    }

    /**
     * 设置
     * @param nodeMetadataUUID
     */
    public void setNodeMetadataUUID(String nodeMetadataUUID) {
        this.nodeMetadataUUID = nodeMetadataUUID;
    }

    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "Node{id = " + id + ", UUID = " + UUID + ", parentUUID = " + parentUUID + ", baseDataUUID = " + baseDataUUID + ", nodeMetadataUUID = " + nodeMetadataUUID + ", type = " + type + "}";
    }
}
