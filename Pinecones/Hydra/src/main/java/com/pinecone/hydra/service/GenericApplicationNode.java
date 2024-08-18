package com.pinecone.hydra.service;

import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public class GenericApplicationNode implements ApplicationNode {
    // 应用节点id
    private long enumId;

    // 应用节点UUID
    private GUID UUID;

    // 应用节点名称
    private String name;


    public GenericApplicationNode() {
    }

    public GenericApplicationNode(long enumId, GUID UUID, String name) {
        this.enumId = enumId;
        this.UUID = UUID;
        this.name = name;
    }

    /**
     * 获取
     * @return enumId
     */
    public long getEnumId() {
        return enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return UUID
     */
    public GUID getUUID() {
        return UUID;
    }

    /**
     * 设置
     * @param UUID
     */
    public void setUUID(GUID UUID) {
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
        return "GenericApplicationNode{enumId = " + enumId + ", UUID = " + UUID + ", name = " + name + "}";
    }
}