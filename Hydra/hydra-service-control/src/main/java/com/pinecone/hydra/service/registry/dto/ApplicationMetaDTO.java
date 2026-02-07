package com.pinecone.hydra.service.registry.dto;

public class ApplicationMetaDTO {

    private String guid;

    private String name;

    private String type;

    private String displayName;

    private String alias;

    private String fullName;

    private String deploymentMethod;

    private String resourceType;

    private String level;




    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDeploymentMethod() {
        return this.deploymentMethod;
    }

    public void setDeploymentMethod(String deploymentMethod) {
        this.deploymentMethod = deploymentMethod;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
