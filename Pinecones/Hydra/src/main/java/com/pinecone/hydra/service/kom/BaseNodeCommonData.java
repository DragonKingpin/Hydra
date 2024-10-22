package com.pinecone.hydra.service.kom;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

public class BaseNodeCommonData implements NodeCommonData {
    private long enumId;

    private GUID guid;

    private String scenario;

    private String primaryImplLang;

    private String extraInformation;

    private String level;

    private String description;


    public BaseNodeCommonData() {
    }


    @Override
    public long getEnumId() {
        return this.enumId;
    }


    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    @Override
    public GUID getGuid() {
        return this.guid;
    }


    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    @Override
    public String getScenario() {
        return this.scenario;
    }


    @Override
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }


    @Override
    public String getPrimaryImplLang() {
        return this.primaryImplLang;
    }


    @Override
    public void setPrimaryImplLang(String primaryImplLang) {
        this.primaryImplLang = primaryImplLang;
    }


    @Override
    public String getExtraInformation() {
        return this.extraInformation;
    }


    @Override
    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }


    @Override
    public String getLevel() {
        return this.level;
    }


    @Override
    public void setLevel(String level) {
        this.level = level;
    }


    @Override
    public String getDescription() {
        return this.description;
    }


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