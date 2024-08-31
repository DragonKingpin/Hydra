package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;

import java.util.List;

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
    private List<GUID> parentGUID;
    //基础信息uuid
    private GUID baseDataGUID;
    //元信息uuid
    private GUID nodeMetadataGUID;
    //节点的类型方便获取数据
    private UOI type;


    public GUIDDistributedScopeNode() {
    }

    public GUIDDistributedScopeNode(long enumId, GUID guid, List<GUID> parentGUID, GUID baseDataGUID, GUID nodeMetadataGUID, UOI type) {
        this.enumId = enumId;
        this.guid = guid;
        this.parentGUID = parentGUID;
        this.baseDataGUID = baseDataGUID;
        this.nodeMetadataGUID = nodeMetadataGUID;
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
     * @return parentGUID
     */
    public List<GUID> getParentGUIDs() {
        return parentGUID;
    }

    /**
     * 设置
     * @param parentGUID
     */
    public void setParentGUID(List<GUID> parentGUID) {
        this.parentGUID = parentGUID;
    }

    /**
     * 获取
     * @return baseDataGUID
     */
    public GUID getBaseDataGUID() {
        return baseDataGUID;
    }

    /**
     * 设置
     * @param baseDataGUID
     */
    public void setBaseDataGUID(GUID baseDataGUID) {
        this.baseDataGUID = baseDataGUID;
    }

    /**
     * 获取
     * @return nodeMetadataGUID
     */
    public GUID getNodeMetadataGUID() {
        return nodeMetadataGUID;
    }

    /**
     * 设置
     * @param nodeMetadataGUID
     */
    public void setNodeMetadataGUID(GUID nodeMetadataGUID) {
        this.nodeMetadataGUID = nodeMetadataGUID;
    }

    /**
     * 获取
     * @return type
     */
    public UOI getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(UOI type) {
        this.type = type;
    }

    public String toString() {
        return "GUIDDistributedScopeNode{enumId = " + enumId + ", guid = " + guid + ", parentGUID = " + parentGUID + ", baseDataGUID = " + baseDataGUID + ", nodeMetadataGUID = " + nodeMetadataGUID + ", type = " + type + "}";
    }
}
