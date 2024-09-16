package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericTextValue implements TextValue {
    private int enumId;
    private GUID guid;
    private String value;
    private String type;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public GenericTextValue() {
    }

    public GenericTextValue(int enumId, GUID guid, String value, String type, LocalDateTime createTime, LocalDateTime updateTime) {
        this.enumId = enumId;
        this.guid = guid;
        this.value = value;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
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
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
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

    public String toString() {
        return "GenericTextValue{enumId = " + enumId + ", guid = " + guid + ", value = " + value + ", type = " + type + ", createTime = " + createTime + ", updateTime = " + updateTime + "}";
    }
}
