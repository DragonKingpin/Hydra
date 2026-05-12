package com.pinecone.hydra.deploy.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.entity.GenericNamespace;
import com.pinecone.hydra.deploy.kom.entity.Namespace;
import com.pinecone.hydra.deploy.kom.source.DeployNamespaceManipulator;


import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
@IbatisDataAccessObject
public interface DeployNamespaceMapper extends DeployNamespaceManipulator {

    @Override
    void insert( Namespace ns );

    @Override
    void remove( @Param("guid") GUID GUID );

    @Override
    GenericNamespace getNamespace( @Param("guid") GUID guid );

    @Override
    void update( Namespace ns );

    List<GenericNamespace > fetchNamespaceNodeByName0( @Param("name") String name );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<Namespace > fetchNamespaceNodeByName( String name ){
        return (List) this.fetchNamespaceNodeByName0( name );
    }

    @Override
    List<GUID > getGuidsByName(@Param("name") String name);

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );

}
