package com.pinecone.hydra.service.tree.nodes;


import com.pinecone.framework.util.id.GUID;

public class GenericClassificationNode implements ClassificationNode {
    // 节点id
    private long enumId;

    // 节点uuid
    private GUID guid;

    // 节点名称
    private String name;

    // 分类规则uuid
    private GUID rulesGUID;


    public GenericClassificationNode() {
    }

    public GenericClassificationNode(long enumId, GUID guid, String name, GUID rulesGUID) {
        this.enumId = enumId;
        this.guid = guid;
        this.name = name;
        this.rulesGUID = rulesGUID;
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

    /**
     * 获取
     * @return rulesGUID
     */
    public GUID getRulesGUID() {
        return rulesGUID;
    }

    /**
     * 设置
     * @param rulesGUID
     */
    public void setRulesGUID(GUID rulesGUID) {
        this.rulesGUID = rulesGUID;
    }

    public String toString() {
        return "GenericClassificationNode{enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", rulesGUID = " + rulesGUID + "}";
    }
}
