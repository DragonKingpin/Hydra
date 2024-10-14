package com.pinecone.hydra.service.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

public class GenericNodeCommonData implements NodeCommonData {
    // id
    private long enumId;

    // 元信息uuid
    private GUID guid;

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


    public GenericNodeCommonData() {
    }

    public GenericNodeCommonData( long enumId, GUID guid, String scenario, String primaryImplLang, String extraInformation, String level, String description ) {
        this.enumId = enumId;
        this.guid = guid;
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
    @Override
    public long getEnumId() {
        return this.enumId;
    }

    /**
     * 设置
     * @param enumId
     */
    @Override
    public void setEnumId(long enumId) {
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
     * @return scenario
     */
    @Override
    public String getScenario() {
        return this.scenario;
    }

    /**
     * 设置
     * @param scenario
     */
    @Override
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    /**
     * 获取
     * @return primaryImplLang
     */
    @Override
    public String getPrimaryImplLang() {
        return this.primaryImplLang;
    }

    /**
     * 设置
     * @param primaryImplLang
     */
    @Override
    public void setPrimaryImplLang(String primaryImplLang) {
        this.primaryImplLang = primaryImplLang;
    }

    /**
     * 获取
     * @return extraInformation
     */
    @Override
    public String getExtraInformation() {
        return this.extraInformation;
    }

    /**
     * 设置
     * @param extraInformation
     */
    @Override
    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    /**
     * 获取
     * @return level
     */
    @Override
    public String getLevel() {
        return this.level;
    }

    /**
     * 设置
     * @param level
     */
    @Override
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取
     * @return description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置
     * @param description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }
}