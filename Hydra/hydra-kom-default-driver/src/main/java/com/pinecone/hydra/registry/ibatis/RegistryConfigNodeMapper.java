package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.GenericTextFile;
import com.pinecone.hydra.registry.source.RegistryConfigNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryConfigNodeMapper extends RegistryConfigNodeManipulator {
    void insert( ConfigNode configNode );

    void remove( @Param("guid") GUID guid );

    @Override
    boolean isConfigNode( @Param("guid") GUID guid );

    UOI getUOIByGUID( @Param("guid") GUID guid );

    GenericProperties getPropertiesNode( @Param("guid") GUID guid );

    GenericTextFile getTextConfigNode( @Param("guid") GUID guid );

    @Override
    default ConfigNode getConfigNode (GUID guid ) {
        String objectName = this.getUOIByGUID(guid).getObjectName();
        if ( objectName.equals( GenericTextFile.class.getName()) ){
            return this.getTextConfigNode(guid);
        }
        else if ( objectName.equals(GenericProperties.class.getName()) ){
            return this.getPropertiesNode(guid);
        }
        return null;
    }

    @Override
    default void update( ConfigNode configNode ) {
        if (configNode.getUpdateTime() != null){
            this.updateUpdateTime(configNode.getUpdateTime(),configNode.getGuid());
        }
        if (configNode.getName() != null){
            //updateName(configNode.getName(),configNode.getGuid());
        }
    }

    @Override
    List<GUID > getGuidsByName( @Param("name") String name );

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );

    void updateUpdateTime( @Param("updateTime") LocalDateTime updateTime, @Param("guid") GUID guid );

    List<GUID > dumpGuid();

    void updateName( @Param("guid") GUID guid, @Param("name") String name );

    GUID getDataAffinityGuid ( @Param("guid") GUID guid );

    void setDataAffinityGuid( @Param("guid") GUID guid, @Param("affinityGuid") GUID affinityGuid );

}
