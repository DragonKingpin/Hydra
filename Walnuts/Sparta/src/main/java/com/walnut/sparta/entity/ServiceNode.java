package com.walnut.sparta.entity;

public class ServiceNode {
    //服务节点id
    private String id;
    //服务节点UUID
    private String UUID;
    //服务节点名称
    private String name;

    public ServiceNode() {
    }

    public ServiceNode(String id, String UUID, String name) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
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

    public String toString() {
        return "ServiceNode{id = " + id + ", UUID = " + UUID + ", name = " + name + "}";
    }
}
