package com.pinecone.hydra.service;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class GenericServiceDescription implements ServiceDescription {
    // 服务id
    private String id;

    // 服务uuid
    private String UUID;

    // 服务名称
    private String name;

    // 服务路径
    private String path;

    // 类型
    private String type;

    // 服务别名
    private String alias;

    // 资源类型
    private String resourceType;

    // 服务类型
    private String serviceType;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 最近更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public GenericServiceDescription() {
    }

    public GenericServiceDescription(String id, String UUID, String name, String path, String type, String alias, String resourceType, String serviceType, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
        this.path = path;
        this.type = type;
        this.alias = alias;
        this.resourceType = resourceType;
        this.serviceType = serviceType;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 获取
     * @return id
     */
    @Override
    public String getId() {
        return this.id;
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
        return this.UUID;
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
     * @return path
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * 设置
     * @param path
     */
    @Override
    public void setPath(String path) {
        this.path = path;
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
     * @return alias
     */
    @Override
    public String getAlias() {
        return this.alias;
    }

    /**
     * 设置
     * @param alias
     */
    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取
     * @return resourceType
     */
    @Override
    public String getResourceType() {
        return this.resourceType;
    }

    /**
     * 设置
     * @param resourceType
     */
    @Override
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * 获取
     * @return serviceType
     */
    @Override
    public String getServiceType() {
        return this.serviceType;
    }

    /**
     * 设置
     * @param serviceType
     */
    @Override
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    @Override
    public String toString() {
        return "GenericServiceDescription{id = " + this.id + ", UUID = " + this.UUID + ", name = " + this.name + ", path = " + this.path + ", type = " + this.type + ", alias = " + this.alias + ", resourceType = " + this.resourceType + ", serviceType = " + this.serviceType + ", createTime = " + this.createTime + ", updateTime = " + this.updateTime + "}";
    }
}
