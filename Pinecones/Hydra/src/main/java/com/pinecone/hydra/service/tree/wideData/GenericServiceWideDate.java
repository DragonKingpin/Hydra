package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.util.id.GUID;

public class GenericServiceWideDate implements NodeWideData {
    private GUID guid;
    private GUID parentGUID;
    private String name;
    private String path;
    private String type;
    private String alias;
    private String resourceType;
    private String serviceType;
    private String createTime;
    private String updateTime;
    private String scenario;
    private String primaryImplLang;
    private String extraInformation;
    private String level;
    private String description;

    public GenericServiceWideDate() {
    }

    public GenericServiceWideDate(GUID guid, GUID parentGUID, String name, String path, String type, String alias, String resourceType, String serviceType, String createTime, String updateTime, String scenario, String primaryImplLang, String extraInformation, String level, String description) {
        this.guid = guid;
        this.parentGUID = parentGUID;
        this.name = name;
        this.path = path;
        this.type = type;
        this.alias = alias;
        this.resourceType = resourceType;
        this.serviceType = serviceType;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.scenario = scenario;
        this.primaryImplLang = primaryImplLang;
        this.extraInformation = extraInformation;
        this.level = level;
        this.description = description;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public GUID getParentGUID() {
        return parentGUID;
    }


    public void setParentGUID(GUID parentGUID) {
        this.parentGUID = parentGUID;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getAlias() {
        return alias;
    }


    public void setAlias(String alias) {
        this.alias = alias;
    }


    public String getResourceType() {
        return resourceType;
    }


    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public String getServiceType() {
        return serviceType;
    }


    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    public String getCreateTime() {
        return createTime;
    }


    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    public String getScenario() {
        return scenario;
    }


    public void setScenario(String scenario) {
        this.scenario = scenario;
    }


    public String getPrimaryImplLang() {
        return primaryImplLang;
    }


    public void setPrimaryImplLang(String primaryImplLang) {
        this.primaryImplLang = primaryImplLang;
    }


    public String getExtraInformation() {
        return extraInformation;
    }


    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public String getLevel() {
        return level;
    }


    public void setLevel(String level) {
        this.level = level;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "GenericServiceNodeCache{guid = " + guid + ", parentGUID = " + parentGUID + ", name = " + name + ", path = " + path + ", type = " + type + ", alias = " + alias + ", resourceType = " + resourceType + ", serviceType = " + serviceType + ", createTime = " + createTime + ", updateTime = " + updateTime + ", scenario = " + scenario + ", primaryImplLang = " + primaryImplLang + ", extraInformation = " + extraInformation + ", level = " + level + ", description = " + description + "}";
    }
}
