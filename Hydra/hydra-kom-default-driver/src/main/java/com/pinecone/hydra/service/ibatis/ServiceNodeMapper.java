package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ServiceNodeMapper extends ServiceNodeManipulator {

    void insert( GenericServiceElement serviceNode );

    @Override
    void remove( @Param("guid")GUID guid );

    @Override
    GenericServiceElement getServiceNode( @Param("guid") GUID guid );

    void update( GenericServiceElement serviceNode );

    List<GenericServiceElement> fetchServiceNodeByName0( @Param("name") String name );

    @Override
    @SuppressWarnings("unchecked")
    default List<ServiceElement> fetchServiceNodeByName( String name ) {
        return (List) this.fetchServiceNodeByName0( name );
    }



    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );


    @Override
    @SuppressWarnings("unchecked")
    default List<ServiceElement> fetchAllService(){
        return (List) this.fetchAllService0();
    }

    List<GenericServiceElement> fetchAllService0();
}
