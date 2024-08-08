package com.walnut.sparta.entity;

import java.util.UUID;
public class classification_rules {
    //规则id
    private String id;
    //规则uuid
    private UUID uuid;
    //作用域
    private String scope;
    //名称
    private String name;
    //规则描述
    private String description;


    public classification_rules() {
    }

    public classification_rules(String id, UUID uuid, String scope, String name, String description) {
        this.id = id;
        this.uuid = uuid;
        this.scope = scope;
        this.name = name;
        this.description = description;
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
     * @return scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * 设置
     * @param scope
     */
    public void setScope(String scope) {
        this.scope = scope;
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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "classification_rules{id = " + id + ", uuid = " + uuid + ", scope = " + scope + ", name = " + name + ", description = " + description + "}";
    }
}
