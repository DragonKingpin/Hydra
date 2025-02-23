package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.service.kom.entity.Namespace;

import java.time.LocalDateTime;

public class ServiceMetaDTO implements Pinenut {
   private String guid;

   private String name;

   private String displayName;

   private String description;

   private String fullName;

   private Namespace groupNamespace;

   private String groupName;

   private String scenario;

   private String primaryImplLang;

   private String extraInformation;

   private String level;

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Namespace getGroupNamespace() {
        return this.groupNamespace;
    }

    public void setGroupNamespace(Namespace groupNamespace) {
        this.groupNamespace = groupNamespace;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getScenario() {
        return this.scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getPrimaryImplLang() {
        return this.primaryImplLang;
    }

    public void setPrimaryImplLang(String primaryImplLang) {
        this.primaryImplLang = primaryImplLang;
    }

    public String getExtraInformation() {
        return this.extraInformation;
    }

    public void setExtraInformation(String extraInformation) {
        this.extraInformation = extraInformation;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
