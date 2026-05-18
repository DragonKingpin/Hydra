package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumePhysicalMapper extends VolumePhysicalManipulator {
    @Override
    void insert( VolumePhysical physical );

    @Override
    void update( VolumePhysical physical );

    @Override
    VolumePhysical get( @Param("guid") GUID guid );

    @Override
    List<VolumePhysical> listAll();

    @Override
    void remove( @Param("guid") GUID guid );
}

