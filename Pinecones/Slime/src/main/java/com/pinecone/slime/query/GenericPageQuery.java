package com.pinecone.slime.query;

import com.pinecone.framework.system.Nullable;

public class GenericPageQuery<E> implements PageQuery<E> {
    @Nullable
    private String key;

    private E      value;
    private long   offset;
    private long   pageSize;

    public GenericPageQuery( String key, E value, long offset, long pageSize ) {
        this.key      = key;
        this.value    = value;
        this.offset   = offset;
        this.pageSize = pageSize;
    }

    public GenericPageQuery( E value, long offset, long pageSize ) {
        this.value    = value;
        this.offset   = offset;
        this.pageSize = pageSize;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public void setKey( String key ) {
        this.key = key;
    }

    @Override
    public E getValue() {
        return this.value;
    }

    @Override
    public void setValue( E value ) {
        this.value = value;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public void setOffset( long offset ) {
        this.offset = offset;
    }

    @Override
    public long getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize( long pageSize ) {
        this.pageSize = pageSize;
    }

}
