package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface ApplicationNodeMapper extends ApplicationNodeManipulator {
    void insert( ApplicationElement applicationElement );

    void remove( @Param("guid")GUID guid );

    GenericApplicationElement getApplicationNode(@Param("guid")GUID guid);

    void update( ApplicationElement applicationElement );

    @Override
    List<GUID > getGuidsByName( String name );

    @Override
    List<GUID > getGuidsByNameID( @Param("name") String name, @Param("guid") GUID guid );
}
