package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.source.VolumeExtentManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumeExtentMapper extends VolumeExtentManipulator {
    @Override
    void insert( VolumeExtent extent );

    @Override
    void update( VolumeExtent extent );

    @Override
    VolumeExtent get( @Param("guid") GUID guid );

    @Override
    List<VolumeExtent> listByParentGuid( @Param("parentGuid") GUID parentGuid );

    @Override
    List<VolumeExtent> listByPhysicalGuid( @Param("physicalGuid") GUID physicalGuid );

    @Override
    long countByPhysicalGuid( @Param("physicalGuid") GUID physicalGuid );

    @Override
    long countByChildGuid( @Param("childGuid") GUID childGuid );

    @Override
    void remove( @Param("guid") GUID guid );

    @Override
    void removeByParentGuid( @Param("parentGuid") GUID parentGuid );
}

