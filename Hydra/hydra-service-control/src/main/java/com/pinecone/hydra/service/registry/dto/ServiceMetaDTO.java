package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.service.Service;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.service.kom.entity.Namespace;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.ulf.util.guid.GUIDs;

import java.time.LocalDateTime;

public class ServiceMetaDTO implements Pinenut {
    private String guid;

    private String name;

    private String type;

    private String displayName;

    private String description;

    private String fullName;

    private String groupNamespace;

    private String groupName;

    private String scenario;

    private String primaryImplLang;

    private String extraInformation;

    private String level;

    public String getType() {
        return this.type;
    }

    public void setType( String type ) {
        this.type = type;
    }

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

    public String getGroupNamespace() {
        return this.groupNamespace;
    }

    public void setGroupNamespace(String groupNamespace) {
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


    public static ServiceMetaDTO from( Service service ){
        ServiceMetaDTO serviceMetaDTO = new ServiceMetaDTO();
        serviceMetaDTO.setGuid( service.getId().toString() );
        serviceMetaDTO.setName(service.getName());
        serviceMetaDTO.setDescription( service.getDescription() );
        serviceMetaDTO.setDisplayName( service.getDisplayName() );
        serviceMetaDTO.setFullName( service.getFullName() );
        serviceMetaDTO.setExtraInformation( service.getExtraInformation() );
        serviceMetaDTO.setLevel( service.getLevel() );
        serviceMetaDTO.setScenario( service.getScenario() );
        serviceMetaDTO.setPrimaryImplLang( service.getPrimaryImplLang() );
        serviceMetaDTO.setGroupName( service.getGroupName() );
        serviceMetaDTO.setType( service.getType() );
        return serviceMetaDTO;
    }

    public static ServiceMetaDTO from( ServiceElement service ){
        ServiceMetaDTO serviceMetaDTO = new ServiceMetaDTO();
        serviceMetaDTO.setGuid( service.getId().toString() );
        serviceMetaDTO.setName(service.getName());
        serviceMetaDTO.setDescription( service.getDescription() );
        serviceMetaDTO.setDisplayName( service.getName() );
        serviceMetaDTO.setFullName( service.getPath() );
        serviceMetaDTO.setExtraInformation( service.getExtraInformation() );
        serviceMetaDTO.setLevel( service.getLevel() );
        serviceMetaDTO.setScenario( service.getScenario() );
        serviceMetaDTO.setPrimaryImplLang( service.getPrimaryImplLang() );
        serviceMetaDTO.setGroupName( null );
        serviceMetaDTO.setType( service.getType() );
        return serviceMetaDTO;
    }

    public static ServiceElement toServiceElement( ServiceMetaDTO meta, GuidAllocator guidAllocator ) {
        ServiceElement element = new GenericServiceElement();
        if ( meta.getGuid() != null ) {
            element.setGuid( guidAllocator.parse(meta.getGuid()) );
        }
        element.setName( meta.getName());
        element.setDescription( meta.getDescription() );
        element.setExtraInformation( meta.getExtraInformation() );
        element.setLevel( meta.getLevel() );
        element.setScenario( meta.getScenario() );
        element.setPrimaryImplLang( meta.getPrimaryImplLang() );
        element.setType( meta.getType() );
        return element;
    }

}
