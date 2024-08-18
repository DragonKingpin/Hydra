package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public class GUIDDistributedScopeNode implements DistributedScopeNode {
    //节点id
    private long enumId;
    //节点uuid
    private GUID UUID;
    //父节点uuid
    private GUID parentUUID;
    //基础信息uuid
    private GUID baseDataUUID;
    //元信息uuid
    private GUID nodeMetadataUUID;
    //节点的类型方便获取数据
    private String type;


    public GUIDDistributedScopeNode() {
    }

    public GUIDDistributedScopeNode(long enumId, GUID UUID, GUID parentUUID, GUID baseDataUUID, GUID nodeMetadataUUID, String type) {
        this.enumId = enumId;
        this.UUID = UUID;
        this.parentUUID = parentUUID;
        this.baseDataUUID = baseDataUUID;
        this.nodeMetadataUUID = nodeMetadataUUID;
        this.type = type;
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
     * @return parentUUID
     */
    public GUID getParentUUID() {
        return parentUUID;
    }

    /**
     * 设置
     * @param parentUUID
     */
    public void setParentUUID(GUID parentUUID) {
        this.parentUUID = parentUUID;
    }

    /**
     * 获取
     * @return baseDataUUID
     */
    public GUID getBaseDataUUID() {
        return baseDataUUID;
    }

    /**
     * 设置
     * @param baseDataUUID
     */
    public void setBaseDataUUID(GUID baseDataUUID) {
        this.baseDataUUID = baseDataUUID;
    }

    /**
     * 获取
     * @return nodeMetadataUUID
     */
    public GUID getNodeMetadataUUID() {
        return nodeMetadataUUID;
    }

    /**
     * 设置
     * @param nodeMetadataUUID
     */
    public void setNodeMetadataUUID(GUID nodeMetadataUUID) {
        this.nodeMetadataUUID = nodeMetadataUUID;
    }

    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "GUIDDistributedScopeNode{enumId = " + enumId + ", UUID = " + UUID + ", parentUUID = " + parentUUID + ", baseDataUUID = " + baseDataUUID + ", nodeMetadataUUID = " + nodeMetadataUUID + ", type = " + type + "}";
    }
}
