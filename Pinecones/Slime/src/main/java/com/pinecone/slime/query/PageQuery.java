package com.pinecone.slime.query;

import com.pinecone.framework.system.prototype.Pinenut;

public interface PageQuery<E> extends Pinenut {

    String getKey();

    void setKey( String key );

    E getValue();

    void setValue( E value );

    long getOffset();

    void setOffset( long offset );

    long getPageSize();

    void setPageSize( long pageSize );

}
