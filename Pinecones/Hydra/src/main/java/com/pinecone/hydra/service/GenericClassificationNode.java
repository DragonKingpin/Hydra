package com.pinecone.hydra.service;


public class GenericClassificationNode implements ClassificationNode {
    // 节点id
    private String id;

    // 节点uuid
    private String UUID;

    // 节点名称
    private String name;

    // 分类规则uuid
    private String rulesUUID;

    public GenericClassificationNode() {
    }

    public GenericClassificationNode(String id, String UUID, String name, String rulesUUID) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
        this.rulesUUID = rulesUUID;
    }

    /**
     * 获取
     * @return id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取
     * @return UUID
     */
    @Override
    public String getUUID() {
        return UUID;
    }

    /**
     * 设置
     * @param UUID
     */
    @Override
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * 获取
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return rulesUUID
     */
    @Override
    public String getRulesUUID() {
        return rulesUUID;
    }

    /**
     * 设置
     * @param rulesUUID
     */
    @Override
    public void setRulesUUID(String rulesUUID) {
        this.rulesUUID = rulesUUID;
    }

    @Override
    public String toString() {
        return "GenericClassificationNode{id = " + id + ", UUID = " + UUID + ", name = " + name + ", rulesUUID = " + rulesUUID + "}";
    }
}
