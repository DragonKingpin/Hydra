package com.pinecone.hydra.device.generic;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class GenericDeviceType implements Pinenut {
    protected long enumId;
    protected GUID guid;
    protected String code;
    protected String name;
    protected String description;
    protected String category;
    protected String parentTypeCode;
    protected GUID schemaGuid;
    protected boolean enabled = true;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public long getEnumId() {
        return this.enumId;
    }

    public void setEnumId( long enumId ) {
        this.enumId = enumId;
    }

    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public String getParentTypeCode() {
        return this.parentTypeCode;
    }

    public void setParentTypeCode( String parentTypeCode ) {
        this.parentTypeCode = parentTypeCode;
    }

    public GUID getSchemaGuid() {
        return this.schemaGuid;
    }

    public void setSchemaGuid( GUID schemaGuid ) {
        this.schemaGuid = schemaGuid;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }
}
