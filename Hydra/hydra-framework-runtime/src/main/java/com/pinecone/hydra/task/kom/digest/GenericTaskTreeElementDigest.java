package com.pinecone.hydra.task.kom.digest;

import com.pinecone.framework.util.id.GUID;

public class GenericTaskTreeElementDigest implements TaskTreeElementDigest {

    protected long enumId;

    protected GUID guid;

    protected GUID parentGuid;

    protected String name;

    protected String type;

    protected String path;

    protected String longPath;

    protected int childrenCount;

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public void setEnumId( long nEnumId ) {
        this.enumId = nEnumId;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    @Override
    public GUID getParentGuid() {
        return this.parentGuid;
    }

    @Override
    public void setParentGuid( GUID parentGuid ) {
        this.parentGuid = parentGuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName( String szName ) {
        this.name = szName;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType( String szType ) {
        this.type = szType;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath( String szPath ) {
        this.path = szPath;
    }

    @Override
    public String getLongPath() {
        return this.longPath;
    }

    @Override
    public void setLongPath( String szLongPath ) {
        this.longPath = szLongPath;
    }

    @Override
    public int getChildrenCount() {
        return this.childrenCount;
    }

    @Override
    public void setChildrenCount( int nChildrenCount ) {
        this.childrenCount = nChildrenCount;
    }
}
