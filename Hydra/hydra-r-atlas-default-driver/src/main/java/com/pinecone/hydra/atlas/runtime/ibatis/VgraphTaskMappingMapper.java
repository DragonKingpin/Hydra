package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.source.VgraphTaskMappingManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@IbatisDataAccessObject
public interface VgraphTaskMappingMapper extends VgraphTaskMappingManipulator {
    @Override
    @Insert("INSERT INTO `hydra_atlas_vgraph_task_mapping` (`task_guid`, `vgraph_node_guid`) VALUES (#{taskGuid}, #{vgraphNodeGuid})")
    void insert(@Param("taskGuid") GUID taskGuid, @Param("vgraphNodeGuid") GUID vgraphNodeGuid );

    @Override
    @Select("SELECT `vgraph_node_guid` FROM `hydra_atlas_vgraph_task_mapping` WHERE `task_guid` = #{taskGuid}")
    GUID queryVgraphNodeGuid( GUID taskGuid );

    @Override
    @Select("SELECT `task_guid` FROM `hydra_atlas_vgraph_task_mapping` WHERE `vgraph_node_guid` = #{vgraphNodeGuid}")
    GUID queryTaskGuid( GUID vgraphNodeGuid );

    @Override
    @Delete("DELETE FROM `hydra_atlas_vgraph_task_mapping` WHERE `task_guid` = #{taskGuid} AND `vgraph_node_guid` = #{vgraphNodeGuid}")
    void remove( @Param("taskGuid") GUID taskGuid, @Param("vgraphNodeGuid") GUID vgraphNodeGuid );

    @Override
    @Delete("DELETE FROM `hydra_atlas_vgraph_task_mapping` WHERE `vgraph_node_guid` = #{vgraphNodeGuid}")
    void removeByVgraphNodeGuid( GUID vgraphNodeGuid );

    @Override
    @Delete("DELETE FROM `hydra_atlas_vgraph_task_mapping` WHERE `task_guid` = #{taskGuid}")
    void removeByTaskGuid( GUID taskGuid );
}
