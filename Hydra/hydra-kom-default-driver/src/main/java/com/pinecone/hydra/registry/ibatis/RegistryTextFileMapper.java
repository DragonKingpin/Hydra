package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericTextValue;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.source.RegistryTextFileManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface RegistryTextFileMapper extends RegistryTextFileManipulator {
    void insert( TextValue textValue );

    void remove( @Param("guid") GUID guid );

    GenericTextValue getTextValue( @Param("guid") GUID guid );

    void update( TextValue textValue );

    void copyTextValueTo( @Param("sourceGuid") GUID sourceGuid, @Param("destinationGuid") GUID destinationGuid );
}
