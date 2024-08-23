package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;

public class GenericServiceNode implements ServiceNode {
    // 服务节点id
    private long enumId;

    // 服务节点UUID
    private GUID UUID;

    // 服务节点名称
    private String name;


    public GenericServiceNode() {
    }

    public GenericServiceNode(long enumId, GUID UUID, String name) {
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
        return "GenericServiceNode{enumId = " + enumId + ", UUID = " + UUID + ", name = " + name + "}";
    }
}