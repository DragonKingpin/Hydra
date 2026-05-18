package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericNamespace;
import com.pinecone.hydra.registry.entity.Namespace;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface RegistryNSNodeMapper extends RegistryNSNodeManipulator {
    @Override
    void insert( Namespace namespace );

    @Override
    void remove( @Param("guid") GUID guid );

    @Override
    boolean isNamespaceNode( @Param("guid") GUID guid );

    @Override
    GenericNamespace getNamespaceWithMeta( @Param("guid") GUID guid );

    @Override
    void update( Namespace namespace );

    @Override
    List<GUID > getGuidsByName( @Param("name") String name );

    @Override
    List<GUID > getGuidsByNameID( @Param( "name" ) String name, @Param( "guid" ) GUID guid );

    @Override
    List<GUID > dumpGuid();

    @Override
    void updateName( @Param("guid") GUID guid, @Param("name") String name );
}
