package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalSupportTrait;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalSupportTraitManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumePhysicalSupportTraitMapper extends VolumePhysicalSupportTraitManipulator {
    @Override
    void insert( VolumePhysicalSupportTrait trait );

    @Override
    void update( VolumePhysicalSupportTrait trait );

    @Override
    VolumePhysicalSupportTrait get( @Param("guid") GUID guid );

    @Override
    VolumePhysicalSupportTrait getByPhysicalGuid( @Param("physicalGuid") GUID physicalGuid );

    @Override
    List<VolumePhysicalSupportTrait> listAll();

    @Override
    List<VolumePhysicalSupportTrait> listByCode( @Param("code") String code );
}
