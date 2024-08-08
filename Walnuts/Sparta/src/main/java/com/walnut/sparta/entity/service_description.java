package com.walnut.sparta.entity;

import java.time.LocalDateTime;
import java.util.UUID;
public class service_description {
    //服务id
    private String id;
    //服务uuid
    private UUID uuid;
    //服务名称
    private String name;
    //服务路径
    private String path;
    //类型
    private String type;
    //服务别名
    private String alias;
    //资源类型
    private String resource_type;
    //服务类型
    private String service_type;
    //创建时间
    private LocalDateTime create_time;
    //最近更新时间
    private LocalDateTime update_time;

    public service_description() {
    }

    public service_description(String id, UUID uuid, String name, String path, String type, String alias, String resource_type, String service_type, LocalDateTime create_time, LocalDateTime update_time) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.path = path;
        this.type = type;
        this.alias = alias;
        this.resource_type = resource_type;
        this.service_type = service_type;
        this.create_time = create_time;
        this.update_time = update_time;
    }

    /**
     * 获取
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * 设置
     * @param uuid
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
     * @return resource_type
     */
    public String getResource_type() {
        return resource_type;
    }

    /**
     * 设置
     * @param resource_type
     */
    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    /**
     * 获取
     * @return service_type
     */
    public String getService_type() {
        return service_type;
    }

    /**
     * 设置
     * @param service_type
     */
    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    /**
     * 获取
     * @return create_time
     */
    public LocalDateTime getCreate_time() {
        return create_time;
    }

    /**
     * 设置
     * @param create_time
     */
    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取
     * @return update_time
     */
    public LocalDateTime getUpdate_time() {
        return update_time;
    }

    /**
     * 设置
     * @param update_time
     */
    public void setUpdate_time(LocalDateTime update_time) {
        this.update_time = update_time;
    }

    public String toString() {
        return "service_baseInformation{id = " + id + ", uuid = " + uuid + ", name = " + name + ", path = " + path + ", type = " + type + ", alias = " + alias + ", resource_type = " + resource_type + ", service_type = " + service_type + ", create_time = " + create_time + ", update_time = " + update_time + "}";
    }
}
