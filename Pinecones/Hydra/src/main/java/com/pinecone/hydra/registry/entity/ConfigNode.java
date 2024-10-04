package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.DistributedRegistry;
import java.time.LocalDateTime;

public interface ConfigNode extends RegistryTreeNode {
    @Override
    default ConfigNode evinceConfigNode() {
        return this;
    }

    long getEnumId();

    void setEnumId(long enumId);

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getDataAffinityGuid();

    void setDataAffinityGuid( GUID guid );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    String getName();

    void setName( String name );

    void copyTo( String path );

    void copyTo( GUID guid );

    void copyMetaTo( GUID guid );



    ConfigNodeMeta getConfigNodeMeta();

    void setConfigNodeMeta( GenericConfigNodeMeta configNodeMeta );

    NodeAttribute getNodeCommonData();

    void setNodeCommonData( GenericNodeAttribute nodeCommonData );



    DistributedRegistry getRegistry();


}
