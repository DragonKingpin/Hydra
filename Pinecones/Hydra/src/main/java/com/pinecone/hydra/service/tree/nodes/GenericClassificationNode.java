package com.pinecone.hydra.service.tree.nodes;


import com.pinecone.framework.util.id.GUID;

public class GenericClassificationNode implements ClassificationNode {
    // 节点id
    private long enumId;

    // 节点uuid
    private GUID UUID;

    // 节点名称
    private String name;

    // 分类规则uuid
    private GUID rulesUUID;


    public GenericClassificationNode() {
    }

    public GenericClassificationNode(long enumId, GUID UUID, String name, GUID rulesUUID) {
        this.enumId = enumId;
        this.UUID = UUID;
        this.name = name;
        this.rulesUUID = rulesUUID;
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

    /**
     * 获取
     * @return rulesUUID
     */
    public GUID getRulesUUID() {
        return rulesUUID;
    }

    /**
     * 设置
     * @param rulesUUID
     */
    public void setRulesUUID(GUID rulesUUID) {
        this.rulesUUID = rulesUUID;
    }

    public String toString() {
        return "GenericClassificationNode{enumId = " + enumId + ", UUID = " + UUID + ", name = " + name + ", rulesUUID = " + rulesUUID + "}";
    }
}
