package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;

import java.time.LocalDateTime;

public class GenericApplicationWideEntityMeta implements MetaNodeWideEntity {
    private GUID guid;
    private GUID parentGUID;
    private String name;
    private String path;
    private String type;
    private String resourceType;
    private String deploymentMethod;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String scenario;
    private String primaryImplLang;
    private String extraInformation;
    private String level;
    private String description;
    private UOI nodeType;


    public GenericApplicationWideEntityMeta() {
    }

    public GenericApplicationWideEntityMeta(GUID guid, GUID parentGUID, String name, String path, String type, String resourceType, String deploymentMethod, LocalDateTime createTime, LocalDateTime updateTime, String scenario, String primaryImplLang, String extraInformation, String level, String description, UOI nodeType) {
        this.guid = guid;
        this.parentGUID = parentGUID;
        this.name = name;
        this.path = path;
        this.type = type;
        this.resourceType = resourceType;
        this.deploymentMethod = deploymentMethod;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.scenario = scenario;
        this.primaryImplLang = primaryImplLang;
        this.extraInformation = extraInformation;
        this.level = level;
        this.description = description;
        this.nodeType = nodeType;
    }

    @Override
    public GUID getParentGUIDs() {
        return parentGUID;
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


    public String getResourceType() {
        return resourceType;
    }


    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public String getDeploymentMethod() {
        return deploymentMethod;
    }


    public void setDeploymentMethod(String deploymentMethod) {
        this.deploymentMethod = deploymentMethod;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
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


    public UOI getNodeType() {
        return nodeType;
    }


    public void setNodeType(UOI nodeType) {
        this.nodeType = nodeType;
    }

    public String toString() {
        return "GenericApplicationWideEntityMeta{guid = " + guid + ", parentGUID = " + parentGUID + ", name = " + name + ", path = " + path + ", type = " + type + ", resourceType = " + resourceType + ", deploymentMethod = " + deploymentMethod + ", createTime = " + createTime + ", updateTime = " + updateTime + ", scenario = " + scenario + ", primaryImplLang = " + primaryImplLang + ", extraInformation = " + extraInformation + ", level = " + level + ", description = " + description + ", nodeType = " + nodeType + "}";
    }
}
