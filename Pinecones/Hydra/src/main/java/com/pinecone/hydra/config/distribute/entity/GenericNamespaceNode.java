package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.Name;

import java.time.LocalDateTime;

public class GenericNamespaceNode implements NamespaceNode {
    private int enumId;
    private GUID guid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    private GenericNamespaceNodeMeta namespaceNodeMeta;
    private GenericNodeCommonData nodeCommonData;


    public GenericNamespaceNode() {
    }

    public GenericNamespaceNode(int enumId, GUID guid, LocalDateTime createTime, LocalDateTime updateTime, String name, GenericNamespaceNodeMeta namespaceNodeMeta, GenericNodeCommonData nodeCommonData) {
        this.enumId = enumId;
        this.guid = guid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.namespaceNodeMeta = namespaceNodeMeta;
        this.nodeCommonData = nodeCommonData;
    }

    /**
     * 获取
     * @return enumId
     */
    public int getEnumId() {
        return enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    public void setEnumId(int enumId) {
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
     * @return createTime
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return updateTime
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置
     * @param updateTime
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
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
     * @return namespaceNodeMeta
     */
    public GenericNamespaceNodeMeta getNamespaceNodeMeta() {
        return namespaceNodeMeta;
    }

    /**
     * 设置
     * @param namespaceNodeMeta
     */
    public void setNamespaceNodeMeta(GenericNamespaceNodeMeta namespaceNodeMeta) {
        this.namespaceNodeMeta = namespaceNodeMeta;
    }

    /**
     * 获取
     * @return nodeCommonData
     */
    public GenericNodeCommonData getNodeCommonData() {
        return nodeCommonData;
    }

    /**
     * 设置
     * @param nodeCommonData
     */
    public void setNodeCommonData(GenericNodeCommonData nodeCommonData) {
        this.nodeCommonData = nodeCommonData;
    }

    public String toString() {
        return "GenericNamespaceNode{enumId = " + enumId + ", guid = " + guid + ", createTime = " + createTime + ", updateTime = " + updateTime + ", name = " + name + ", namespaceNodeMeta = " + namespaceNodeMeta + ", nodeCommonData = " + nodeCommonData + "}";
    }
}
