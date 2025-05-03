package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.source.QueueStratumManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface QueueStratumMapper extends QueueStratumManipulator {

    @Override
    @Select("SELECT `segment_name` FROM `hydra_atlas_queue_stratum` WHERE `vgraph_guid` = #{vgraphGuid}" +
            " AND `stratum_id` = #{stratumId} AND `runtime_priority` = #{runtimePriority}")
    String querySegmentName(@Param("vgraphGuid") GUID vgraphGuid, @Param("stratumId") short stratumId,
                            @Param("runtimePriority") short runtimePriority);

    @Override
    @Select("SELECT `stratum_id` FROM `hydra_atlas_queue_stratum` ORDER BY `stratum_id` DESC LIMIT 1")
    int countStratum(GUID vgraphGuid);

    @Override
    @Select("SELECT `runtime_priority` FROM `hydra_atlas_queue_stratum` ORDER BY `runtime_priority` DESC LIMIT 1")
    int countPriority(@Param("vgraphGuid") GUID vgraphGuid, @Param("stratumId") short stratumId);
}
