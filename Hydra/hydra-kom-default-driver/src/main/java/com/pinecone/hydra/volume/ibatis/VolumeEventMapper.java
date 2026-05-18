package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeEvent;
import com.pinecone.hydra.storage.volume.source.VolumeEventManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumeEventMapper extends VolumeEventManipulator {
    @Override
    void insert( VolumeEvent event );

    @Override
    VolumeEvent get( @Param("guid") GUID guid );

    @Override
    List<VolumeEvent> listByVolumeGuid( @Param("volumeGuid") GUID volumeGuid );

    @Override
    List<VolumeEvent> listByPhysicalGuid( @Param("physicalGuid") GUID physicalGuid );
}

