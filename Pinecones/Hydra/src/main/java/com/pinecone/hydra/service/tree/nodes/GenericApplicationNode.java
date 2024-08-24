package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.util.id.GUID;

public class GenericApplicationNode implements ApplicationNode {
    // 应用节点id
    private long enumId;

    // 应用节点UUID
    private GUID guid;

    // 应用节点名称
    private String name;


    public GenericApplicationNode() {
    }

    public GenericApplicationNode(long enumId, GUID guid, String name) {
        this.enumId = enumId;
        this.guid = guid;
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
     * @return guid
     */
    public GUID getGuid() {
        return guid;
    }

    /**
     * 设置
     * @param guid
     */
    public void setGuid(GUID guid) {
        this.guid = guid;
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
        return "GenericApplicationNode{enumId = " + enumId + ", guid = " + guid + ", name = " + name + "}";
    }
}