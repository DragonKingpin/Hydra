package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface Property extends Pinenut {
    static Property newDummy() {
        return new GenericProperty();
    }

    long getEnumId();

    void setEnumId( long enumId );

    GUID getGuid();

    void setGuid( GUID guid );

    String getKey();

    void setKey( String key );

    String getType();

    void setType( String type );

    Object getRawValue();

    void setRawValue( Object value );

    Object getValue();

    void setValue( Object value );

    boolean isStringBasedType() ;

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void  setUpdateTime( LocalDateTime updateTime );

    // Not copy guid
    void fromValue ( Property that );

    void from      ( Property that );

    void copy      ( Property that );

    Properties parentProperties();

    void setParentProperties( Properties parentProperties );
}
