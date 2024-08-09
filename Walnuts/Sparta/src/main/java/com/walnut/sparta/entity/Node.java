package com.walnut.sparta.entity;

import java.util.UUID;

public class Node {
    //节点id
    private String id;
    //节点uuid
    private String UUID;
    //节点名称
    private String name;
    //父节点uuid
    private UUID parentUUID;
    //基础信息uuid
    private UUID baseDataUUID;
    //元信息uuid
    private UUID nodeMetadataUUID;


    public Node() {
    }

    public Node(String id, String UUID, String name, UUID parentUUID, UUID baseDataUUID, UUID nodeMetadataUUID) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
        this.parentUUID = parentUUID;
        this.baseDataUUID = baseDataUUID;
        this.nodeMetadataUUID = nodeMetadataUUID;
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
     * @return parentUUID
     */
    public UUID getParentUUID() {
        return parentUUID;
    }

    /**
     * 设置
     * @param parentUUID
     */
    public void setParentUUID(UUID parentUUID) {
        this.parentUUID = parentUUID;
    }

    /**
     * 获取
     * @return baseDataUUID
     */
    public UUID getBaseDataUUID() {
        return baseDataUUID;
    }

    /**
     * 设置
     * @param baseDataUUID
     */
    public void setBaseDataUUID(UUID baseDataUUID) {
        this.baseDataUUID = baseDataUUID;
    }

    /**
     * 获取
     * @return nodeMetadataUUID
     */
    public UUID getNodeMetadataUUID() {
        return nodeMetadataUUID;
    }

    /**
     * 设置
     * @param nodeMetadataUUID
     */
    public void setNodeMetadataUUID(UUID nodeMetadataUUID) {
        this.nodeMetadataUUID = nodeMetadataUUID;
    }

    public String toString() {
        return "node{id = " + id + ", UUID = " + UUID + ", name = " + name + ", parentUUID = " + parentUUID + ", baseDataUUID = " + baseDataUUID + ", nodeMetadataUUID = " + nodeMetadataUUID + "}";
    }
}
