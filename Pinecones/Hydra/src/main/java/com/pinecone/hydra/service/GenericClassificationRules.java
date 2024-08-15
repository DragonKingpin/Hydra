package com.pinecone.hydra.service;

public class GenericClassificationRules implements ClassificationRules {
    // 规则id
    private String id;

    // 规则uuid
    private String UUID;

    // 作用域
    private String scope;

    // 名称
    private String name;

    // 规则描述
    private String description;

    public GenericClassificationRules() {
    }

    public GenericClassificationRules(String id, String UUID, String scope, String name, String description) {
        this.id = id;
        this.UUID = UUID;
        this.scope = scope;
        this.name = name;
        this.description = description;
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
     * @return scope
     */
    @Override
    public String getScope() {
        return scope;
    }

    /**
     * 设置
     * @param scope
     */
    @Override
    public void setScope(String scope) {
        this.scope = scope;
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
     * @return description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     * @param description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "GenericClassificationRules{id = " + id + ", UUID = " + UUID + ", scope = " + scope + ", name = " + name + ", description = " + description + "}";
    }
}