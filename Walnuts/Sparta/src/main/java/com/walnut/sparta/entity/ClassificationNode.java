package com.walnut.sparta.entity;

import java.util.UUID;
public class ClassificationNode {
    //节点id
    private String id;
    //节点uuid
    private String UUID;
    //节点名称
    private String name;
    //分类规则uuid
    private UUID rulesUUID;


    public ClassificationNode() {
    }

    public ClassificationNode(String id, String UUID, String name, UUID rulesUUID) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
        this.rulesUUID = rulesUUID;
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
     * @return rulesUUID
     */
    public UUID getRulesUUID() {
        return rulesUUID;
    }

    /**
     * 设置
     * @param rulesUUID
     */
    public void setRulesUUID(UUID rulesUUID) {
        this.rulesUUID = rulesUUID;
    }

    public String toString() {
        return "classificationNode{id = " + id + ", UUID = " + UUID + ", name = " + name + ", rulesUUID = " + rulesUUID + "}";
    }
}
