package com.walnut.sparta.entity;

import java.util.UUID;
public class classification_node {
    //节点id
    private String id;
    //节点uuid
    private UUID uuid;
    //节点名称
    private String name;
    //分类规则uuid
    private UUID rules_uuid;

    public classification_node() {
    }

    public classification_node(String id, UUID uuid, String name, UUID rules_uuid) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.rules_uuid = rules_uuid;
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
    public UUID getUuid() {
        return uuid;
    }

    /**
     * 设置
     * @param uuid
     */
    public void setUuid(UUID uuid) {
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
     * @return rules_uuid
     */
    public UUID getRules_uuid() {
        return rules_uuid;
    }

    /**
     * 设置
     * @param rules_uuid
     */
    public void setRules_uuid(UUID rules_uuid) {
        this.rules_uuid = rules_uuid;
    }

    public String toString() {
        return "classification_node{id = " + id + ", uuid = " + uuid + ", name = " + name + ", rules_uuid = " + rules_uuid + "}";
    }
}
