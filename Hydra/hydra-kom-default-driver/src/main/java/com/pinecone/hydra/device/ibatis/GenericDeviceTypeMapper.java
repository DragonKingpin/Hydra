package com.pinecone.hydra.device.ibatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.hydra.device.generic.GenericDeviceType;
import com.pinecone.hydra.device.kom.source.GenericDeviceTypeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import java.util.Collection;

@Mapper
@IbatisDataAccessObject
public interface GenericDeviceTypeMapper extends GenericDeviceTypeManipulator {

    @Override
    void insert( GenericDeviceType type );

    @Override
    void update( GenericDeviceType type );

    @Override
    GenericDeviceType getGenericDeviceTypeByCode( @Param("code") String code );

    @Override
    Collection<GenericDeviceType> fetchGenericDeviceTypes();
}
