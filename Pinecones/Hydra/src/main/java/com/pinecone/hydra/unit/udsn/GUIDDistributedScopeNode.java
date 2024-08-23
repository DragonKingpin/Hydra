package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONEncoder;

/**
 *  Pinecone Ursus For Java GUIDDistributedScopeNode
 *  Author:
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  **********************************************************
 *  Thanks for genius`s contribution.
 *  **********************************************************
 */
public class GUIDDistributedScopeNode implements DistributedTreeNode {
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
    @Override
    public long getEnumId() {
        return this.enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return UUID
     */
    @Override
    public GUID getUUID() {
        return this.UUID;
    }

    /**
     * 设置
     * @param UUID
     */
    @Override
    public void setUUID(GUID UUID) {
        this.UUID = UUID;
    }

    /**
     * 获取
     * @return parentUUID
     */
    @Override
    public GUID getParentUUID() {
        return this.parentUUID;
    }

    /**
     * 设置
     * @param parentUUID
     */
    @Override
    public void setParentUUID(GUID parentUUID) {
        this.parentUUID = parentUUID;
    }

    /**
     * 获取
     * @return baseDataUUID
     */
    @Override
    public GUID getBaseDataUUID() {
        return this.baseDataUUID;
    }

    /**
     * 设置
     * @param baseDataUUID
     */
    @Override
    public void setBaseDataUUID(GUID baseDataUUID) {
        this.baseDataUUID = baseDataUUID;
    }

    /**
     * 获取
     * @return nodeMetadataUUID
     */
    @Override
    public GUID getNodeMetadataUUID() {
        return this.nodeMetadataUUID;
    }

    /**
     * 设置
     * @param nodeMetadataUUID
     */
    @Override
    public void setNodeMetadataUUID(GUID nodeMetadataUUID) {
        this.nodeMetadataUUID = nodeMetadataUUID;
    }

    /**
     * 获取
     * @return type
     */
    @Override
    public String getType() {
        return this.type;
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
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "EnumId"           , this.enumId            ),
                new KeyValue<>( "UUID"             , this.UUID              ),
                new KeyValue<>( "ParentUUID"       , this.parentUUID        ),
                new KeyValue<>( "BaseDataUUID"     , this.baseDataUUID      ),
                new KeyValue<>( "NodeMetadataUUID" , this.nodeMetadataUUID  ),
                new KeyValue<>( "Type"             , this.type              ),
        } );
    }
}
