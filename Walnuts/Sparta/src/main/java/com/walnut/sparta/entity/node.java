package com.walnut.sparta.entity;

import java.util.UUID;

public class node {
    //节点id
    private String id;
    //节点uuid
    private String uuid;
    //节点名称
    private String name;
    //父节点uuid
    private UUID parent_uuid;
    //基础信息uuid
    private UUID base_data_uuid;
    //元信息uuid
    private UUID node_metadata_uuid;


    public node() {
    }

    public node(String id, String uuid, String name, UUID parent_uuid, UUID base_data_uuid, UUID node_metadata_uuid) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.parent_uuid = parent_uuid;
        this.base_data_uuid = base_data_uuid;
        this.node_metadata_uuid = node_metadata_uuid;
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
     * @return uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
     * @return parent_uuid
     */
    public UUID getParent_uuid() {
        return parent_uuid;
    }

    /**
     * 设置
     * @param parent_uuid
     */
    public void setParent_uuid(UUID parent_uuid) {
        this.parent_uuid = parent_uuid;
    }

    /**
     * 获取
     * @return base_data_uuid
     */
    public UUID getBase_data_uuid() {
        return base_data_uuid;
    }

    /**
     * 设置
     * @param base_data_uuid
     */
    public void setBase_data_uuid(UUID base_data_uuid) {
        this.base_data_uuid = base_data_uuid;
    }

    /**
     * 获取
     * @return node_metadata_uuid
     */
    public UUID getNode_metadata_uuid() {
        return node_metadata_uuid;
    }

    /**
     * 设置
     * @param node_metadata_uuid
     */
    public void setNode_metadata_uuid(UUID node_metadata_uuid) {
        this.node_metadata_uuid = node_metadata_uuid;
    }

    public String toString() {
        return "node{id = " + id + ", uuid = " + uuid + ", name = " + name + ", parent_uuid = " + parent_uuid + ", base_data_uuid = " + base_data_uuid + ", node_metadata_uuid = " + node_metadata_uuid + "}";
    }
}
