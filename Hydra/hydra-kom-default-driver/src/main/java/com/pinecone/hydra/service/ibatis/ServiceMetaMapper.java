package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.source.ServiceMetaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
@IbatisDataAccessObject
public interface ServiceMetaMapper extends ServiceMetaManipulator {
    void insert( ServiceElement serviceElement );

    void remove( @Param("guid") GUID guid );


    GenericServiceElement getServiceMeta( @Param("guid") GUID guid );

    void update( ServiceElement serviceElement );

    void updateName( @Param("name") String name, @Param("guid") GUID guid );

    void updatePath( @Param("path") String path, @Param("guid") GUID guid );

    void updateAlias( @Param("alias") String alias, @Param("guid") GUID guid );

    void updateResourceType( @Param("resourceType") String resourceType, @Param("guid") GUID guid );

    void updateServiceType( @Param("serviceType") String serviceType, @Param("guid") GUID guid );

    void updateUpdateTime( @Param("updateTime") LocalDateTime updateTime, @Param("guid") GUID guid );

}
