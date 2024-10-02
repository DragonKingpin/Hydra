package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface Property extends Pinenut {
    int getEnumId();

    void setEnumId( int enumId );

    GUID getGuid();

    void setGuid( GUID guid );

    String getKey();

    void setKey( String key );

    String getType();

    void setType( String type );

    Object getRawValue();

    Object getValue();

    void setRawValue( Object value );

    void setValue( Object value );

    boolean isStringBasedType() ;

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void  setUpdateTime( LocalDateTime updateTime );
}