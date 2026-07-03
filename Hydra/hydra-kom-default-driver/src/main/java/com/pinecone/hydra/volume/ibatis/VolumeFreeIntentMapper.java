package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeFreeIntent;
import com.pinecone.hydra.storage.volume.core.VolumeFreeIntentStatus;
import com.pinecone.hydra.storage.volume.source.VolumeFreeIntentManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface VolumeFreeIntentMapper extends VolumeFreeIntentManipulator {
    @Override
    void insert( VolumeFreeIntent intent );

    @Override
    void updateStatus(
            @Param("guid") GUID guid,
            @Param("status") VolumeFreeIntentStatus status,
            @Param("message") String message
    );

    @Override
    VolumeFreeIntent get( @Param("guid") GUID guid );

    @Override
    VolumeFreeIntent getBySourceLocationGuid( @Param("sourceLocationGuid") GUID sourceLocationGuid );

    @Override
    long countAll();

    @Override
    List<VolumeFreeIntent> listPage( @Param("offset") int offset, @Param("limit") int limit );

    @Override
    List<VolumeFreeIntent> listByVolumeGuid( @Param("volumeGuid") GUID volumeGuid );

    @Override
    List<VolumeFreeIntent> listByVolumeGuidAndStatus(
            @Param("volumeGuid") GUID volumeGuid,
            @Param("status") VolumeFreeIntentStatus status
    );
}
