package com.pinecone.hydra.service;

public class GenericApplicationNode implements ApplicationNode {
    // 应用节点id
    private String id;

    // 应用节点UUID
    private String UUID;

    // 应用节点名称
    private String name;

    public GenericApplicationNode() {
    }

    public GenericApplicationNode(String id, String UUID, String name) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
    }

    /**
     * 获取
     * @return id
     */
    @Override
    public String getId() {
        return this.id;
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
        return this.UUID;
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
        return this.name;
    }

    /**
     * 设置
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GenericApplicationNode{id = " + this.id + ", UUID = " + this.UUID + ", name = " + this.name + "}";
    }
}