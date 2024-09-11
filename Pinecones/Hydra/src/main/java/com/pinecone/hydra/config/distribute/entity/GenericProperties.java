package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericProperties implements Properties{
    private int enumId;
    private GUID guid;
    private String key;
    private String type;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String value;

    public GenericProperties() {
    }

    public GenericProperties(int enumId, GUID guid, String key, String type, LocalDateTime createTime, LocalDateTime updateTime, String value) {
        this.enumId = enumId;
        this.guid = guid;
        this.key = key;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.value = value;
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
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
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

    public String toString() {
        return "GenericProperties{enumId = " + enumId + ", guid = " + guid + ", key = " + key + ", type = " + type + ", createTime = " + createTime + ", updateTime = " + updateTime + ", value = " + value + "}";
    }
}
