package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.GenericNamespace;
import com.pinecone.hydra.service.kom.entity.Namespace;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceNamespaceMapper extends ServiceNamespaceManipulator {
    void insert( Namespace ns );

    void remove( @Param("guid") GUID guid );

    GenericNamespace getNamespace( @Param("guid") GUID guid );

    void update( Namespace ns );

    List<GenericNamespace > fetchNamespaceNodeByName0( @Param("name") String name );

    @SuppressWarnings( "unchecked" )
    default List<Namespace > fetchNamespaceNodeByName( String name ){
        return (List) this.fetchNamespaceNodeByName0( name );
    }

    @Override
    List<GUID > getGuidsByName(String name);

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
