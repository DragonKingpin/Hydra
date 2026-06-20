package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.LinkedHashMap;
import java.util.Map;

public class StorageLifecycleDependency implements Pinenut {
    protected String              mszType;
    protected String              mszGuid;
    protected String              mszName;
    protected String              mszStatus;
    protected String              mszRelation;
    protected Map<String, Object> mExt;

    public String getType() {
        return this.mszType;
    }

    public void setType( String type ) {
        this.mszType = type;
    }

    public String getGuid() {
        return this.mszGuid;
    }

    public void setGuid( String guid ) {
        this.mszGuid = guid;
    }

    public String getName() {
        return this.mszName;
    }

    public void setName( String name ) {
        this.mszName = name;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String status ) {
        this.mszStatus = status;
    }

    public String getRelation() {
        return this.mszRelation;
    }

    public void setRelation( String relation ) {
        this.mszRelation = relation;
    }

    public Map<String, Object> getExt() {
        return this.mExt;
    }

    public void setExt( Map<String, Object> ext ) {
        this.mExt = ext;
    }

    public void putExt( String key, Object value ) {
        if ( this.mExt == null ) {
            this.mExt = new LinkedHashMap<>();
        }
        this.mExt.put( key, value );
    }
}
