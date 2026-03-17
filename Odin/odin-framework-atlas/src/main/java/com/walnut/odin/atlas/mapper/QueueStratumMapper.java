package com.walnut.odin.atlas.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface QueueStratumMapper extends QueueStratumManipulator {

    @Override
    String querySegmentName(@Param("vgraphGuid") GUID vgraphGuid, @Param("stratumId") short stratumId,
                            @Param("runtimePriority") short runtimePriority);

    @Override
    Integer countStratum( GUID vgraphGuid );

    @Override
    Integer countPriority( @Param("vgraphGuid") GUID vgraphGuid, @Param("stratumId") short stratumId );

    @Override
    void put(GUID vgraphGuid, short stratumId, short runtimePriority, String segmentName);

}
