package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.Name;

import java.time.LocalDateTime;
import java.util.List;

public class GenericConfNode implements ConfNode {
    private int enumId;
    private GUID guid;
    private GUID nsGuid;
    private GUID parentGuid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    List<GenericProperties> properties;
    TextValue textValue;
    private GenericConfNodeMeta confNodeMeta;
    private GenericNodeCommonData nodeCommonData;

    public GenericConfNode() {
    }

    public GenericConfNode(int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime, LocalDateTime updateTime, String name, List<GenericProperties> properties, TextValue textValue, GenericConfNodeMeta confNodeMeta, GenericNodeCommonData nodeCommonData) {
        this.enumId = enumId;
        this.guid = guid;
        this.nsGuid = nsGuid;
        this.parentGuid = parentGuid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.properties = properties;
        this.textValue = textValue;
        this.confNodeMeta = confNodeMeta;
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
     * @return nsGuid
     */
    public GUID getNsGuid() {
        return nsGuid;
    }

    /**
     * 设置
     * @param nsGuid
     */
    public void setNsGuid(GUID nsGuid) {
        this.nsGuid = nsGuid;
    }

    /**
     * 获取
     * @return parentGuid
     */
    public GUID getParentGuid() {
        return parentGuid;
    }

    /**
     * 设置
     * @param parentGuid
     */
    public void setParentGuid(GUID parentGuid) {
        this.parentGuid = parentGuid;
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
     * @return properties
     */
    public List<GenericProperties> getProperties() {
        return properties;
    }

    /**
     * 设置
     * @param properties
     */
    public void setProperties(List<GenericProperties> properties) {
        this.properties = properties;
    }

    /**
     * 获取
     * @return textValue
     */
    public TextValue getTextValue() {
        return textValue;
    }

    /**
     * 设置
     * @param textValue
     */
    public void setTextValue(TextValue textValue) {
        this.textValue = textValue;
    }

    /**
     * 获取
     * @return confNodeMeta
     */
    public GenericConfNodeMeta getConfNodeMeta() {
        return confNodeMeta;
    }

    /**
     * 设置
     * @param confNodeMeta
     */
    public void setConfNodeMeta(GenericConfNodeMeta confNodeMeta) {
        this.confNodeMeta = confNodeMeta;
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
        return "GenericConfNode{enumId = " + enumId + ", guid = " + guid + ", nsGuid = " + nsGuid + ", parentGuid = " + parentGuid + ", createTime = " + createTime + ", updateTime = " + updateTime + ", name = " + name + ", properties = " + properties + ", textValue = " + textValue + ", confNodeMeta = " + confNodeMeta + ", nodeCommonData = " + nodeCommonData + "}";
    }
}
