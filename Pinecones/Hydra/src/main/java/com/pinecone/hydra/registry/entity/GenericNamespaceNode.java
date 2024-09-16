package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

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

    public GenericNamespaceNode( int enumId, GUID guid, LocalDateTime createTime, LocalDateTime updateTime, String name, GenericNamespaceNodeMeta namespaceNodeMeta, GenericNodeCommonData nodeCommonData ) {
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
    @Override
    public int getEnumId() {
        return this.enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    @Override
    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return guid
     */
    @Override
    public GUID getGuid() {
        return this.guid;
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
     * @return createTime
     */
    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return updateTime
     */
    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置
     * @param updateTime
     */
    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取
     * @return name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return namespaceNodeMeta
     */
    @Override
    public GenericNamespaceNodeMeta getNamespaceNodeMeta() {
        return namespaceNodeMeta;
    }

    /**
     * 设置
     * @param namespaceNodeMeta
     */
    @Override
    public void setNamespaceNodeMeta(GenericNamespaceNodeMeta namespaceNodeMeta) {
        this.namespaceNodeMeta = namespaceNodeMeta;
    }

    /**
     * 获取
     * @return nodeCommonData
     */
    @Override
    public GenericNodeCommonData getNodeCommonData() {
        return nodeCommonData;
    }

    /**
     * 设置
     * @param nodeCommonData
     */
    @Override
    public void setNodeCommonData(GenericNodeCommonData nodeCommonData) {
        this.nodeCommonData = nodeCommonData;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
