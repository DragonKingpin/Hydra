package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.source.ApplicationMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
@IbatisDataAccessObject
public interface AppNodeMetaMapper extends ApplicationMetaManipulator {
    void insert( ApplicationElement applicationElement );

    void remove( @Param("guid") GUID guid );

    GenericApplicationElement getApplicationElement( @Param("guid") GUID guid );

    default GenericApplicationElement getApplicationElement( GUID guid, ServiceInstrument serviceInstrument){
        GenericApplicationElement element = this.getApplicationElement( guid );
        element.apply(serviceInstrument);
        return element;
    }
    void update( ApplicationElement applicationElement );

    void updateName( @Param("name") String name, @Param("guid") GUID guid );
    void updatePath( @Param("path") String path, @Param("guid") GUID guid );
    void updateType( @Param("type") String type, @Param("guid") GUID guid );
    void updateAlias( @Param("alias") String alias, @Param("guid") GUID guid );
    void updateResourceType( @Param("resourceType") String resourceType, @Param("guid") GUID guid );
    void updateDeploymentMethod( @Param("deploymentMethod") String deploymentMethod, @Param("guid") GUID guid );
    void updateUpdateTime( @Param("updateTime") LocalDateTime updateTime, @Param("guid") GUID guid );
}
