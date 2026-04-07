package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerHandleManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface LayerHandleMapper extends LayerHandleManipulator {
    @Override
    @Insert("INSERT INTO `hydra_atlas_layer_handle` (`layer_guid`, `handle_guid`, `type`) VALUES (#{layerGuid},#{handleGuid},'source')")
    void insertSourceNode(GUID layerGuid, GUID handleGuid);

    @Override
    @Insert("INSERT INTO `hydra_atlas_layer_handle` (`layer_guid`, `handle_guid`, `type`) VALUES (#{layerGuid},#{handleGuid},'sink')")
    void insertSinkNode(GUID layerGuid, GUID handleGuid);

    @Override
    @Insert({
            "<script>",
            "INSERT INTO `hydra_atlas_layer_handle` (`layer_guid`, `handle_guid`, `type`) VALUES ",
            "<foreach collection='handleGuids' item='handleGuid' separator=','>",
            "(#{layerGuid}, #{handleGuid}, 'source')",
            "</foreach>",
            "</script>"
    })
    void batchInsertSourceNodes(@Param("layerGuid") GUID layerGuid, @Param("handleGuids") List<GUID> handleGuids);

    @Override
    @Insert({
            "<script>",
            "INSERT INTO `hydra_atlas_layer_handle` (`layer_guid`, `handle_guid`, `type`) VALUES ",
            "<foreach collection='handleGuids' item='handleGuid' separator=','>",
            "(#{layerGuid}, #{handleGuid}, 'sink')",
            "</foreach>",
            "</script>"
    })
    void batchInsertSinkNodes(@Param("layerGuid") GUID layerGuid, @Param("handleGuids") List<GUID> handleGuids);

    @Override
    @Select("SELECT `handle_guid` FROM `hydra_atlas_layer_handle` WHERE `layer_guid` = #{layerGuid} AND `type` = 'source'")
    List<GUID> fetchSourceNodes( GUID layerGuid );

    @Override
    @Select("SELECT `handle_guid` FROM `hydra_atlas_layer_handle` WHERE `layer_guid` = #{layerGuid} AND `type` = 'sink'")
    List<GUID> fetchSinkNodes( GUID layerGuid );

    @Override
    @Select("SELECT COUNT(id) FROM `hydra_atlas_layer_handle` WHERE `layer_guid` = #{layerGuid} AND `type` = 'source'")
    long countSourceNode(GUID layerGuid);

    @Override
    @Select("SELECT halh.handle_guid " +
            "FROM hydra_atlas_layer_handle halh " +
            "JOIN hydra_atlas_vgraph_task_mapping vatm ON halh.handle_guid = vatm.vgraph_node_guid " +
            "JOIN hydra_task_task_node httn ON vatm.task_guid = httn.guid " +
            "WHERE halh.layer_guid = #{layerGuid} " +
            "AND halh.type = 'source' " +
            "AND NOT EXISTS (" +
            "    SELECT id FROM hydra_atlas_vgraph_adjacent hava WHERE hava.guid = halh.handle_guid" +
            ") " +
            "ORDER BY httn.priority " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<GUID> fetchSourceGuidsByTaskPriority(GUID layerGuid, long offset, long limit);
}
