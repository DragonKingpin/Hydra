package com.pinecone.hydra.service.tree;

import com.pinecone.framework.util.id.GUID;

public class GenericNodeMetadata implements NodeMetadata {
    // id
    private long enumId;

    // 元信息uuid
    private GUID UUID;

    // 场景
    private String scenario;

    // 主语言
    private String primaryImplLang;

    // 额外信息
    private String extraInformation;

    // 等级
    private String level;

    // 描述
    private String description;


    public GenericNodeMetadata() {
    }

    public GenericNodeMetadata(long enumId, GUID UUID, String scenario, String primaryImplLang, String extraInformation, String level, String description) {
        this.enumId = enumId;
        this.UUID = UUID;
        this.scenario = scenario;
        this.primaryImplLang = primaryImplLang;
        this.extraInformation = extraInformation;
        this.level = level;
        this.description = description;
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
     * @return UUID
     */
    public GUID getUUID() {
        return UUID;
    }

    /**
     * 设置
     * @param UUID
     */
    public void setUUID(GUID UUID) {
        this.UUID = UUID;
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
     * @return primaryImplLang
     */
    public String getPrimaryImplLang() {
        return primaryImplLang;
    }

    /**
     * 设置
     * @param primaryImplLang
     */
    public void setPrimaryImplLang(String primaryImplLang) {
        this.primaryImplLang = primaryImplLang;
    }

    /**
     * 获取
     * @return extraInformation
     */
    public String getExtraInformation() {
        return extraInformation;
    }

    /**
     * 设置
     * @param extraInformation
     */
    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
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
        return "GenericNodeMetadata{enumId = " + enumId + ", UUID = " + UUID + ", scenario = " + scenario + ", primaryImplLang = " + primaryImplLang + ", extraInformation = " + extraInformation + ", level = " + level + ", description = " + description + "}";
    }
}