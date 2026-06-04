package com.pinecone.hydra.device.ibatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.generic.GenericDeviceSchema;
import com.pinecone.hydra.device.kom.source.GenericDeviceSchemaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.Collection;

@Mapper
@IbatisDataAccessObject
public interface GenericDeviceSchemaMapper extends GenericDeviceSchemaManipulator {

    @Override
    void insert( GenericDeviceSchema schema );

    @Override
    void update( GenericDeviceSchema schema );

    @Override
    GenericDeviceSchema getGenericDeviceSchema( @Param("guid") GUID guid );

    @Override
    GenericDeviceSchema getGenericDeviceSchemaByCode( @Param("code") String code );

    @Override
    Collection<GenericDeviceSchema> fetchGenericDeviceSchemas();
}
