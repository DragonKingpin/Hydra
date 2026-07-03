package com.pinecone.hydra.storage.file.query;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FileChildQuery implements Pinenut {
    public static final int DefaultLimit = 200;
    public static final int MaxLimit = 5000;

    protected GUID parentGuid;
    protected List<FileChildType> childTypes;
    protected int offset;
    protected int limit = DefaultLimit;
    protected FileChildOrder orderBy = FileChildOrder.NAME;
    protected boolean ascending = true;

    public static FileChildQuery directories() {
        FileChildQuery query = new FileChildQuery();
        query.setChildTypes( List.of( FileChildType.FOLDER, FileChildType.REPARSE ) );
        return query;
    }

    public FileChildQuery normalized() {
        FileChildQuery query = new FileChildQuery();
        query.setParentGuid( this.parentGuid );
        query.setChildTypes( this.childTypes );
        query.setOffset( this.offset );
        query.setLimit( this.limit );
        query.setOrderBy( this.orderBy );
        query.setAscending( this.ascending );
        return query;
    }

    public GUID getParentGuid() {
        return this.parentGuid;
    }

    public void setParentGuid( GUID parentGuid ) {
        this.parentGuid = parentGuid;
    }

    public List<FileChildType> getChildTypes() {
        return this.childTypes;
    }

    public void setChildTypes( List<FileChildType> childTypes ) {
        this.childTypes = childTypes == null ? null : new ArrayList<>( childTypes );
    }

    public List<String> getTypeNames() {
        if ( this.childTypes == null || this.childTypes.isEmpty() ) {
            return List.of();
        }
        List<String> ret = new ArrayList<>();
        for ( FileChildType childType : this.childTypes ) {
            if ( childType != null ) {
                ret.add( childType.getTypeName() );
            }
        }
        return ret;
    }

    public boolean getHasTypeFilter() {
        return !this.getTypeNames().isEmpty();
    }

    public boolean getIncludeFolder() {
        return this.includes( FileChildType.FOLDER );
    }

    public boolean getIncludeFile() {
        return this.includes( FileChildType.FILE );
    }

    public boolean getIncludeReparse() {
        return this.includes( FileChildType.REPARSE );
    }

    protected boolean includes( FileChildType target ) {
        if ( this.childTypes == null || this.childTypes.isEmpty() ) {
            return false;
        }
        return this.childTypes.contains( target );
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset( int offset ) {
        this.offset = Math.max( 0, offset );
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit( int limit ) {
        if ( limit <= 0 ) {
            this.limit = DefaultLimit;
            return;
        }
        this.limit = Math.min( limit, MaxLimit );
    }

    public FileChildOrder getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy( FileChildOrder orderBy ) {
        this.orderBy = orderBy == null ? FileChildOrder.NAME : orderBy;
    }

    public boolean getAscending() {
        return this.ascending;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public void setAscending( boolean ascending ) {
        this.ascending = ascending;
    }

    public boolean getOrderByCreateTime() {
        return this.orderBy == FileChildOrder.CREATE_TIME;
    }

    public boolean getOrderByUpdateTime() {
        return this.orderBy == FileChildOrder.UPDATE_TIME;
    }
}
