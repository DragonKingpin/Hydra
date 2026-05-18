package com.pinecone.hydra.business.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NodeCachePath extends Pinenut {

    long getEnumId();

    void setEnumId( long nEnumId );

    GUID getGuid();

    void setGuid( GUID guid );

    String getPath();

    void setPath( String szPath );

    String getLongPath();

    void setLongPath( String szLongPath );

    default String getResolvedPath() {
        if ( this.getLongPath() == null ) {
            return this.getPath();
        }

        return this.getLongPath();
    }

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
