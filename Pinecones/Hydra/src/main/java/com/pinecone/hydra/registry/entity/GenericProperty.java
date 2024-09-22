package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

import java.time.LocalDateTime;

public class GenericProperty implements Property {
    private int enumId;
    private GUID guid;
    private String key;
    private String type;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String value;

    public GenericProperty() {
    }

    public GenericProperty(int enumId, GUID guid, String key, String type, LocalDateTime createTime, LocalDateTime updateTime, String value ) {
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
    @Override
    public int getEnumId() {
        return enumId;
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
     * @return key
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * 设置
     * @param key
     */
    @Override
    public void setKey(String key) {
        this.key = key;
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
     * @return value
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * 设置
     * @param value
     */
    @Override
    public void setValue(String value) {
        this.value = value;
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
