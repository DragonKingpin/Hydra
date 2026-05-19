package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeMount;
import com.pinecone.hydra.storage.volume.source.VolumeMountManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumeMountMapper extends VolumeMountManipulator {
    @Override
    void insert( VolumeMount mount );

    @Override
    void update( VolumeMount mount );

    @Override
    VolumeMount get( @Param("guid") GUID guid );

    @Override
    VolumeMount getByPath( @Param("mountPath") String mountPath );

    @Override
    List<VolumeMount> listByVolumeGuid( @Param("volumeGuid") GUID volumeGuid );

    @Override
    List<VolumeMount> listPage(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    @Override
    long count( @Param("keyword") String keyword, @Param("status") String status );

    @Override
    void remove( @Param("guid") GUID guid );
}

