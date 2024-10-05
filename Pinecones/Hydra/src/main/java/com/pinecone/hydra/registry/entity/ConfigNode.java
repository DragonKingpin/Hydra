package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.KOMRegistry;
import java.time.LocalDateTime;

public interface ConfigNode extends ElementNode {
    @Override
    default ConfigNode evinceConfigNode() {
        return this;
    }

    void setEnumId( long enumId );

    void setGuid( GUID guid );

    GUID getDataAffinityGuid();

    void setDataAffinityGuid( GUID guid );

    void setCreateTime( LocalDateTime createTime );

    void setUpdateTime( LocalDateTime updateTime );

    void setName( String name );


    void copyMetaTo( GUID guid );



    ConfigNodeMeta getConfigNodeMeta();

    void setConfigNodeMeta( ConfigNodeMeta configNodeMeta );

    void setAttributes( Attributes attributes );



    KOMRegistry parentRegistry();


}
