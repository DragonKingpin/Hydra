package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.source.VolumeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumeMapper extends VolumeManipulator {
    @Override
    void insert( VolumeRecord volumeRecord );

    @Override
    void update( VolumeRecord volumeRecord );

    @Override
    VolumeRecord get( @Param("guid") GUID guid );

    @Override
    List<VolumeRecord> listAll();

    @Override
    void remove( @Param("guid") GUID guid );
}

