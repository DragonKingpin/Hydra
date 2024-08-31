package com.pinecone.hydra.service.tree.meta;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericApplicationNodeMeta implements ApplicationNodeMeta {
    // 应用ID
    private long enumId;

    // 应用UUID
    private GUID guid;

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
    private LocalDateTime createTime;

    // 最近更新时间
    private LocalDateTime updateTime;


    public GenericApplicationNodeMeta() {
    }

    public GenericApplicationNodeMeta(long enumId, GUID guid, String name, String path, String type, String alias, String resourceType, String deploymentMethod, LocalDateTime createTime, LocalDateTime updateTime) {
        this.enumId = enumId;
        this.guid = guid;
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
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
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
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取
     * @return resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * 设置
     * @param resourceType
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * 获取
     * @return deploymentMethod
     */
    public String getDeploymentMethod() {
        return deploymentMethod;
    }

    /**
     * 设置
     * @param deploymentMethod
     */
    public void setDeploymentMethod(String deploymentMethod) {
        this.deploymentMethod = deploymentMethod;
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
        return "GenericApplicationDescription{enumId = " + enumId + ", guid = " + guid + ", name = " + name + ", path = " + path + ", type = " + type + ", alias = " + alias + ", resourceType = " + resourceType + ", deploymentMethod = " + deploymentMethod + ", createTime = " + createTime + ", updateTime = " + updateTime + "}";
    }
}