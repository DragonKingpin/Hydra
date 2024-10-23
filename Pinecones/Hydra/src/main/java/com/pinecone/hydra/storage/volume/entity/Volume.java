package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.time.LocalDateTime;

public interface Volume extends Pinenut {
    long getEnumId();

    GUID getGuid();
    void setGuid(GUID guid);

    LocalDateTime getCreateTime();
    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();
    void setUpdateTime( LocalDateTime updateTime );

    String getName();
    void setName(String name);

    String getType();
    void setType( String type );

    String getExtConfig();
    void setExtConfig( String extConfig );

//    long getDefinitionCapacity();
//    void setDefinitionCapacity( long definitionCapacity );
//    long getUsedSize();
//    void setUsedSize( long usedSize );
//    long getQuotaCapacity();
//    void setQuotaCapacity( long quotaCapacity );
}
