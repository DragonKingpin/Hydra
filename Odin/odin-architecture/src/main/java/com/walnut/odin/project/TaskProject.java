package com.walnut.odin.project;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface TaskProject extends Pinenut {

    long getEnumId();

    void setEnumId( long nEnumId );

    GUID getGuid();

    void setGuid( GUID guid );

    String getName();

    void setName( String szName );

    String getTitle();

    void setTitle( String szTitle );

    GUID getBizTreeGuid();

    void setBizTreeGuid( GUID bizTreeGuid );

    boolean isEnable();

    void setEnable( boolean bEnable );

    String getDescription();

    void setDescription( String szDescription );

    String getExtraInformation();

    void setExtraInformation( String szExtraInformation );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

}
