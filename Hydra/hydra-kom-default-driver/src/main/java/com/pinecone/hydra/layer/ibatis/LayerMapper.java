package com.pinecone.hydra.layer.ibatis;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerGraphHandle;
import com.pinecone.hydra.unit.vgraph.layer.source.LayerManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.meta.TableIndex64Meta;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@IbatisDataAccessObject
public interface LayerMapper extends LayerManipulator {

    @Override
    @Insert(
        "INSERT INTO `hydra_atlas_layer_layers` " +
        "(`layer_guid`, `layer_name`, `update_time`, `create_time`) " +
        "VALUES (#{mGuid}, #{parentGuid}, #{mszName}, #{mUpdateTime}, #{mCreateTime})"
    )
    void insertLayer( LayerGraphHandle layer );

    @Override
    @Insert(
        "<script>" +
        "INSERT INTO `hydra_atlas_layer_layers` " +
        "(`layer_guid`, `layer_name`, `update_time`, `create_time`) VALUES " +
        "<foreach collection='list' item='item' separator=','>" +
        "(#{item.mGuid}, #{item.parentGuid}, #{item.mszName}, #{item.mUpdateTime}, #{item.mCreateTime})" +
        "</foreach>" +
        "</script>"
    )
    void batchInsertLayer( @Param( "list" ) List<LayerGraphHandle> list );

    @Override
    @Delete(
        "DELETE FROM `hydra_atlas_layer_layers` " +
        "WHERE `layer_guid` = #{guid}"
    )
    void remove( GUID guid );



    @Select(
        "SELECT " +
        "l.`id` AS id, " +
        "l.`layer_guid` AS guid, " +
        "t.`parent_guid` AS parentGuid, " +
        "l.`layer_name` AS name, " +
        "l.`update_time` AS updateTime, " +
        "l.`create_time` AS createTime " +
        "FROM `hydra_atlas_layer_layers` l " +
        "LEFT JOIN `hydra_atlas_layer_tree` t ON t.`guid` = l.`layer_guid` " +
        "WHERE l.`layer_guid` = #{guid}"
    )
    AtlasLayer queryLayer0( GUID guid );

    @Override
    @SuppressWarnings( "unchecked" )
    default Layer queryLayer( GUID guid ) {
        return this.queryLayer0( guid );
    }


    @Override
    @Select(
        "SELECT `layer_guid` AS mGuid " +
        "FROM `hydra_atlas_layer_layers` " +
        "WHERE `layer_name` = #{name}"
    )
    List<GUID> getGuidsByName( String name );

    @Override
    @Select(
        "SELECT `layer_guid` AS mGuid " +
        "FROM `hydra_atlas_layer_layers` " +
        "WHERE `layer_name` = #{name} " +
        "AND `layer_guid` = #{guid}"
    )
    List<GUID> getGuidsByNameID( String name, GUID guid );


    @Override
    @SuppressWarnings( "unchecked" )
    default List<Layer> fetchLayer( List<GUID> guids ) {
        return ( List ) this.fetchLayer0( guids );
    }

    @Select(
        "<script>" +
        "SELECT " +
        "l.`id` AS id, " +
        "l.`layer_guid` AS guid, " +
        "t.`parent_guid` AS parentGuid, " +
        "l.`layer_name` AS name, " +
        "l.`update_time` AS updateTime, " +
        "l.`create_time` AS createTime " +
        "FROM `hydra_atlas_layer_layers` l " +
        "LEFT JOIN `hydra_atlas_layer_tree` t ON t.`guid` = l.`layer_guid` " +
        "<where>" +
        "<if test='guids != null and guids.size() > 0'>" +
        "l.`layer_guid` IN " +
        "<foreach item='guid' collection='guids' open='(' separator=',' close=')'>" +
        "#{guid}" +
        "</foreach>" +
        "</if>" +
        "</where>" +
        "</script>"
    )
    List<AtlasLayer> fetchLayer0( List<GUID> guids );









    @Select(
        "<script>" +
        "SELECT " +
        "l.`id` AS id, " +
        "l.`layer_guid` AS guid, " +
        "t.`parent_guid` AS parentGuid, " +
        "l.`layer_name` AS name, " +
        "l.`update_time` AS updateTime, " +
        "l.`create_time` AS createTime " +
        "FROM `hydra_atlas_layer_layers` l " +
        "LEFT JOIN `hydra_atlas_layer_tree` t ON t.`guid` = l.`layer_guid` " +
        "<where>" +
        "<if test=\"anyNode == false\">" +
        "t.`parent_guid` = #{parentGuid} " +
        "</if>" +
        "</where>" +
        "ORDER BY l.`id` ASC " +
        "LIMIT #{limit} OFFSET #{offset}" +
        "</script>"
    )
    List<AtlasLayer> fetchLayerPage0(
        @Param( "offset" ) long offset,
        @Param( "limit" ) long limit,
        @Param( "anyNode" ) boolean anyNode,
        @Param( "parentGuid" ) @Nullable GUID parentGuid
    );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<Layer> fetchLayerPage(
        long offset,
        long limit,
        boolean anyNode,
        @Nullable GUID parentGuid
    ) {
        return ( List ) this.fetchLayerPage0( offset, limit, anyNode, parentGuid );
    }


    @Select(
        "<script>" +
        "SELECT " +
        "l.`id` AS id, " +
        "l.`layer_guid` AS guid, " +
        "t.`parent_guid` AS parentGuid, " +
        "l.`layer_name` AS name, " +
        "l.`update_time` AS updateTime, " +
        "l.`create_time` AS createTime " +
        "FROM `hydra_atlas_layer_layers` l " +
        "LEFT JOIN `hydra_atlas_layer_tree` t ON t.`guid` = l.`layer_guid` " +
        "<where>" +
        "l.`id` &gt;= #{idStart} AND l.`id` &lt;= #{idEnd} " +
        "<if test=\"anyNode == false\">" +
        "AND t.`parent_guid` = #{parentGuid} " +
        "</if>" +
        "</where>" +
        "ORDER BY l.`id` ASC" +
        "</script>"
    )
    List<AtlasLayer> fetchLayerPageById0(
        @Param( "idStart" ) long idStart,
        @Param( "idEnd" ) long idEnd,
        @Param( "anyNode" ) boolean anyNode,
        @Param( "parentGuid" ) @Nullable GUID parentGuid
    );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<Layer> fetchLayerPageById(
        long idStart,
        long idEnd,
        boolean anyNode,
        @Nullable GUID parentGuid
    ) {
        return ( List ) this.fetchLayerPageById0( idStart, idEnd, anyNode, parentGuid );
    }

    @Override
    @Select(
        "<script>" +
        "SELECT " +
        "COALESCE( MIN(l.`id`), 0 ) AS minId, " +
        "COALESCE( MAX(l.`id`), 0 ) AS maxId " +
        "FROM `hydra_atlas_layer_layers` l " +
        "LEFT JOIN `hydra_atlas_layer_tree` t ON t.`guid` = l.`layer_guid` " +
        "<where>" +
        "<if test=\"anyNode == false\">" +
        "t.`parent_guid` = #{parentGuid} " +
        "</if>" +
        "</where>" +
        "</script>"
    )
    TableIndex64Meta selectLayerIndexMeta(
        @Param( "anyNode" ) boolean anyNode,
        @Param( "parentGuid" ) @Nullable GUID parentGuid
    );

    @Override
    @Select(
        "<script>" +
        "SELECT COUNT( * ) " +
        "FROM `hydra_atlas_layer_layers` l " +
        "LEFT JOIN `hydra_atlas_layer_tree` t ON t.`guid` = l.`layer_guid` " +
        "<where>" +
        "<if test=\"anyNode == false\">" +
        "t.`parent_guid` = #{parentGuid} " +
        "</if>" +
        "</where>" +
        "</script>"
    )
    long countLayer(
        @Param( "anyNode" ) boolean anyNode,
        @Param( "parentGuid" ) @Nullable GUID parentGuid
    );

}