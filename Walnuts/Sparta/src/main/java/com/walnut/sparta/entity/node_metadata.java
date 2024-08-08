package com.walnut.sparta.entity;

import java.util.UUID;
public class node_metadata {
    //id
    private String id;
    //元信息uuid
    private UUID uuid;
    //场景
    private String scenario;
    //主语言
    private String primary_impl_lang;
    //额外信息
    private String extra_information;
    //等级
    private String level;
    //描述
    private String description;

    public node_metadata() {
    }

    public node_metadata(String id, UUID uuid, String scenario, String primary_impl_lang, String extra_information, String level, String description) {
        this.id = id;
        this.uuid = uuid;
        this.scenario = scenario;
        this.primary_impl_lang = primary_impl_lang;
        this.extra_information = extra_information;
        this.level = level;
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
     * @return scenario
     */
    public String getScenario() {
        return scenario;
    }

    /**
     * 设置
     * @param scenario
     */
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    /**
     * 获取
     * @return primary_impl_lang
     */
    public String getPrimary_impl_lang() {
        return primary_impl_lang;
    }

    /**
     * 设置
     * @param primary_impl_lang
     */
    public void setPrimary_impl_lang(String primary_impl_lang) {
        this.primary_impl_lang = primary_impl_lang;
    }

    /**
     * 获取
     * @return extra_information
     */
    public String getExtra_information() {
        return extra_information;
    }

    /**
     * 设置
     * @param extra_information
     */
    public void setExtra_information(String extra_information) {
        this.extra_information = extra_information;
    }

    /**
     * 获取
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置
     * @param level
     */
    public void setLevel(String level) {
        this.level = level;
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
        return "node_metadata{id = " + id + ", uuid = " + uuid + ", scenario = " + scenario + ", primary_impl_lang = " + primary_impl_lang + ", extra_information = " + extra_information + ", level = " + level + ", description = " + description + "}";
    }
}
