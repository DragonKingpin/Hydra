package com.pinecone.hydra.service;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class GenericApplicationDescription implements ApplicationDescription {
    // 应用ID
    private String id;

    // 应用UUID
    private String UUID;

    // 应用名称
    private String name;

    // 路径
    private String path;

    // 类型
    private String type;

    // 别名
    private String alias;

    // 资源类型
    private String resourceType;

    // 部署方式
    private String deploymentMethod;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // 最近更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 无参构造方法
     */
    public GenericApplicationDescription() {
    }

    /**
     * 全参构造方法
     * @param id 应用ID
     * @param UUID 应用UUID
     * @param name 应用名称
     * @param path 应用路径
     * @param type 应用类型
     * @param alias 应用别名
     * @param resourceType 资源类型
     * @param deploymentMethod 部署方式
     * @param createTime 创建时间
     * @param updateTime 最近更新时间
     */
    public GenericApplicationDescription(String id, String UUID, String name, String path, String type, String alias, String resourceType, String deploymentMethod, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.UUID = UUID;
        this.name = name;
        this.path = path;
        this.type = type;
        this.alias = alias;
        this.resourceType = resourceType;
        this.deploymentMethod = deploymentMethod;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 获取应用ID
     * @return 应用ID
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * 设置应用ID
     * @param id 应用ID
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取应用UUID
     * @return 应用UUID
     */
    @Override
    public String getUUID() {
        return this.UUID;
    }

    /**
     * 设置应用UUID
     * @param UUID 应用UUID
     */
    @Override
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * 获取应用名称
     * @return 应用名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置应用名称
     * @param name 应用名称
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取应用路径
     * @return 应用路径
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * 设置应用路径
     * @param path 应用路径
     */
    @Override
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取应用类型
     * @return 应用类型
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * 设置应用类型
     * @param type 应用类型
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取应用别名
     * @return 应用别名
     */
    @Override
    public String getAlias() {
        return this.alias;
    }

    /**
     * 设置应用别名
     * @param alias 应用别名
     */
    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取资源类型
     * @return 资源类型
     */
    @Override
    public String getResourceType() {
        return this.resourceType;
    }

    /**
     * 设置资源类型
     * @param resourceType 资源类型
     */
    @Override
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * 获取部署方式
     * @return 部署方式
     */
    @Override
    public String getDeploymentMethod() {
        return this.deploymentMethod;
    }

    /**
     * 设置部署方式
     * @param deploymentMethod 部署方式
     */
    @Override
    public void setDeploymentMethod(String deploymentMethod) {
        this.deploymentMethod = deploymentMethod;
    }

    /**
     * 获取创建时间
     * @return 创建时间
     */
    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最近更新时间
     * @return 最近更新时间
     */
    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置最近更新时间
     * @param updateTime 最近更新时间
     */
    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 返回对象的字符串表示
     * @return 对象的字符串表示
     */
    @Override
    public String toString() {
        return "GenericApplicationDescription{id = " + id + ", UUID = " + UUID + ", name = " + name + ", path = " + path + ", type = " + type + ", alias = " + alias + ", resourceType = " + resourceType + ", deploymentMethod = " + deploymentMethod + ", createTime = " + createTime + ", updateTime = " + updateTime + "}";
    }
}