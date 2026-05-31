package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceQueryDTO implements Pinenut {

    private long offset;

    private long limit;

    private String keyword;

    private String serviceGuid;

    private String type;

    private String alias;

    private String resourceType;

    private String serviceType;

    private String scenario;

    private String primaryImplLang;

    private String level;

    public long getOffset() {
        return this.offset;
    }

    public void setOffset( long offset ) {
        this.offset = offset;
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit( long limit ) {
        this.limit = limit;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword( String keyword ) {
        this.keyword = keyword;
    }

    public String getServiceGuid() {
        return this.serviceGuid;
    }

    public void setServiceGuid( String serviceGuid ) {
        this.serviceGuid = serviceGuid;
    }

    public String getType() {
        return this.type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias( String alias ) {
        this.alias = alias;
    }

    public String getResourceType() {
        return this.resourceType;
    }

    public void setResourceType( String resourceType ) {
        this.resourceType = resourceType;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public void setServiceType( String serviceType ) {
        this.serviceType = serviceType;
    }

    public String getScenario() {
        return this.scenario;
    }

    public void setScenario( String scenario ) {
        this.scenario = scenario;
    }

    public String getPrimaryImplLang() {
        return this.primaryImplLang;
    }

    public void setPrimaryImplLang( String primaryImplLang ) {
        this.primaryImplLang = primaryImplLang;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel( String level ) {
        this.level = level;
    }
}
