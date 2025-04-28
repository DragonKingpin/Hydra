package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface LayerMapper extends LayerManipulator {
    @Override
    @Insert("INSERT INTO `hydra_atlas_layer_layers` (`layer_guid`, `graph_node_guid`, `layer_name`, `update_time`, `create_time`) VALUES (#{mGuid}, #{mHandleNodeGuid},#{mszName}, #{mUpdateTime}, #{mCreateTime})")
    void insertStartLayer( LayerGraphHandle layer );

    @Override
    @Insert("<script>" +
            "INSERT INTO `hydra_atlas_layer_layers` (`layer_guid`, `graph_node_guid`, `layer_name`, `update_time`, `create_time`) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.mGuid}, #{item.mHandleNodeGuid}, #{item.mszName}, #{item.mUpdateTime}, #{item.mCreateTime})" +
            "</foreach>" +
            "</script>")
    void batchInsertLayer(@Param("list") List<LayerGraphHandle> list);

    @Override
    @Delete("DELETE FROM `hydra_atlas_layer_layers` WHERE `layer_guid` = #{guid}")
    void remove( GUID guid );

    @Override
    @Select("SELECT `id` AS enmuId, `layer_guid` AS mGuid, `layer_name` AS mszName, `update_time` AS updateTime, `create_time` AS createTime FROM `hydra_atlas_layer_layers` WHERE `layer_guid` = #{guid} LIMIT 1")
    Layer queryLayer( GUID guid );

    @Override
    @Select("SELECT `layer_guid` AS mGuid  FROM `hydra_atlas_layer_layers` WHERE `layer_name` = #{name}")
    List<GUID > getGuidsByName(String name );

    @Override
    @Select("SELECT `layer_guid` AS mGuid FROM `hydra_atlas_layer_layers` WHERE `layer_name` = #{name} AND `layer_guid` = #{guid}")
    List<GUID > getGuidsByNameID( String name, GUID guid );
}
