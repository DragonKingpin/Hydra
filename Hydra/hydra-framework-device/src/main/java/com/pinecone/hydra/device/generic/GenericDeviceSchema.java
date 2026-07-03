package com.pinecone.hydra.device.generic;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class GenericDeviceSchema implements Pinenut {
    protected long enumId;
    protected GUID guid;
    protected String code;
    protected String name;
    protected String description;
    protected String version = "1.0.0";
    protected String schemaJson;
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

    public String getVersion() {
        return this.version;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    public String getSchemaJson() {
        return this.schemaJson;
    }

    public void setSchemaJson( String schemaJson ) {
        this.schemaJson = schemaJson;
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
