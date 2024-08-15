package com.pinecone.hydra.unit.udsn;

public class UUIDDistributedScopeNode implements DistributedScopeNode {
    //节点id
    private String id;
    //节点uuid
    private String UUID;
    //父节点uuid
    private String parentUUID;
    //基础信息uuid
    private String baseDataUUID;
    //元信息uuid
    private String nodeMetadataUUID;
    //节点的类型方便获取数据
    private String type;


    public UUIDDistributedScopeNode() {
    }

    public UUIDDistributedScopeNode(String id, String UUID, String parentUUID, String baseDataUUID, String nodeMetadataUUID, String type) {
        this.id = id;
        this.UUID = UUID;
        this.parentUUID = parentUUID;
        this.baseDataUUID = baseDataUUID;
        this.nodeMetadataUUID = nodeMetadataUUID;
        this.type = type;
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
     * @return parentUUID
     */
    @Override
    public String getParentUUID() {
        return parentUUID;
    }

    /**
     * 设置
     * @param parentUUID
     */
    @Override
    public void setParentUUID(String parentUUID) {
        this.parentUUID = parentUUID;
    }

    /**
     * 获取
     * @return baseDataUUID
     */
    @Override
    public String getBaseDataUUID() {
        return baseDataUUID;
    }

    /**
     * 设置
     * @param baseDataUUID
     */
    @Override
    public void setBaseDataUUID(String baseDataUUID) {
        this.baseDataUUID = baseDataUUID;
    }

    /**
     * 获取
     * @return nodeMetadataUUID
     */
    @Override
    public String getNodeMetadataUUID() {
        return nodeMetadataUUID;
    }

    /**
     * 设置
     * @param nodeMetadataUUID
     */
    @Override
    public void setNodeMetadataUUID(String nodeMetadataUUID) {
        this.nodeMetadataUUID = nodeMetadataUUID;
    }

    /**
     * 获取
     * @return type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Node{id = " + id + ", UUID = " + UUID + ", parentUUID = " + parentUUID + ", baseDataUUID = " + baseDataUUID + ", nodeMetadataUUID = " + nodeMetadataUUID + ", type = " + type + "}";
    }
}
