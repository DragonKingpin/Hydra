package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;

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
    private GUID guid;
    //父节点uuid
    private GUID parentGUID;
    //基础信息uuid
    private GUID baseDataGUID;
    //元信息uuid
    private GUID nodeMetadataGUID;
    //节点的类型方便获取数据
    private UOI type;

    public GUIDDistributedScopeNode() {
    }

    public GUIDDistributedScopeNode( long enumId, GUID guid, GUID parentGUID, GUID baseDataGUID, GUID nodeMetadataGUID, UOI type ) {
        this.enumId           = enumId;
        this.guid             = guid;
        this.parentGUID       = parentGUID;
        this.baseDataGUID     = baseDataGUID;
        this.nodeMetadataGUID = nodeMetadataGUID;
        this.type             = type;
    }

    /**
     * 获取
     * @return enumId
     */
    @Override
    public long getEnumId() {
        return enumId;
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
     * @return guid
     */
    @Override
    public GUID getGuid() {
        return guid;
    }

    /**
     * 设置
     * @param guid
     */
    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    /**
     * 获取
     * @return parentGUID
     */
    @Override
    public GUID getParentGUID() {
        return parentGUID;
    }

    /**
     * 设置
     * @param parentGUID
     */
    @Override
    public void setParentGUID(GUID parentGUID) {
        this.parentGUID = parentGUID;
    }

    /**
     * 获取
     * @return baseDataGUID
     */
    @Override
    public GUID getBaseDataGUID() {
        return baseDataGUID;
    }

    /**
     * 设置
     * @param baseDataGUID
     */
    @Override
    public void setBaseDataGUID(GUID baseDataGUID) {
        this.baseDataGUID = baseDataGUID;
    }

    /**
     * 获取
     * @return nodeMetadataGUID
     */
    @Override
    public GUID getNodeMetadataGUID() {
        return nodeMetadataGUID;
    }

    /**
     * 设置
     * @param nodeMetadataGUID
     */
    @Override
    public void setNodeMetadataGUID(GUID nodeMetadataGUID) {
        this.nodeMetadataGUID = nodeMetadataGUID;
    }

    /**
     * 获取
     * @return type
     */
    @Override
    public UOI getType() {
        return this.type;
    }

    /**
     * 设置
     * @param type
     */
    @Override
    public void setType( UOI type ) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GUIDDistributedScopeNode{enumId = " + enumId + ", guid = " + guid + ", parentGUID = " + parentGUID + ", baseDataGUID = " + baseDataGUID + ", nodeMetadataGUID = " + nodeMetadataGUID + ", type = " + type + "}";
    }
}
