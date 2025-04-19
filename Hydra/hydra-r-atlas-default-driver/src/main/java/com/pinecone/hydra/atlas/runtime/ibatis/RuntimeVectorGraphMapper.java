package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.entity.TaskAtlasNode;
import com.pinecone.hydra.atlas.entity.TaskGraphNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@IbatisDataAccessObject
public interface RuntimeVectorGraphMapper extends VectorGraphManipulator {
    @Override
    default void insertStartNode( GraphNode graphNode ){
        this.insertGraphNode(graphNode);
    }

    @Insert("INSERT INTO `hydra_atlas_vgraph_nodes` (`guid`, `node_name`, `node_description`) VALUES (#{guid},#{name},#{description})")
    void insertGraphNode( GraphNode graphNode );

    @Insert("INSERT INTO `hydra_atlas_vgraph_adjacent` (`guid`, `linked_type`, `parent_guid`) VALUES (#{childGuid},'weak',#{parentGuid})")
    void insertNodeAdjacent(@Param("parentGuid") GUID parentGuid, @Param("childGuid") GUID childGuid );

    @Override
    default void insertNode(GUID parentGuid, GraphNode graphNode){
        this.insertGraphNode(graphNode);
        this.insertNodeAdjacent(parentGuid,graphNode.getId());
    }


    default void addChild(GUID parentGuid, GraphNode graphNode){
        this.insertNodeAdjacent(parentGuid,graphNode.getId());
    }

    @Override
    default void removeNode( GUID guid ) {
        this.removeGraphNode(guid);
        this.removeGraphAdjacent(guid);
    }

    @Delete("DELETE FROM `hydra_atlas_vgraph_nodes` WHERE `guid` = #{guid}")
    void removeGraphNode( GUID guid );

    @Delete("DELETE FROM `hydra_atlas_vgraph_adjacent` WHERE `guid` = #{guid}")
    void removeGraphAdjacent( GUID guid );

    @Override
    @Select("SELECT `id` AS enumId, `guid`, `node_name` AS name, `node_description` AS description  FROM `hydra_atlas_vgraph_nodes` WHERE `guid` = #{guid}")
    TaskAtlasNode queryNode(GUID guid );

    @Override
    @Select("SELECT `parent_guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `guid` = #{guid}")
    List<GUID> fetchParentIds(GUID guid );

    @Override
    @Select("SELECT havn.`id`,havn.`guid`,havn.`node_name`,havn.`node_description` FROM `hydra_atlas_vgraph_nodes` havn, `hydra_atlas_vgraph_adjacent` hava WHERE hava.`parent_guid` = #{guid} ")
    List<GraphNode> fetchChildNodes( GUID guid );

    @Override
    @Select("SELECT \n" +
            "    havn.`id`,\n" +
            "    havn.`guid`,\n" +
            "    havn.`node_name`,\n" +
            "    havn.`node_description`\n" +
            "FROM \n" +
            "    hydra_atlas_vgraph_nodes havn\n" +
            "WHERE \n" +
            "    NOT EXISTS (\n" +
            "        SELECT 1\n" +
            "        FROM hydra_atlas_vgraph_adjacent hava\n" +
            "        WHERE hava.`guid` = havn.`guid`\n" +
            "    );")
    List<GraphNode> fetchRootNodes();

    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `parent_guid` = #{parentGuid}")
    List<GUID> fetchChildNodeIds(GUID guid );


    @Select("SELECT `id` AS enumId, `guid`, `node_name` AS name, `node_description` AS description  FROM `hydra_atlas_vgraph_nodes` WHERE `node_name` = #{name}")
    List<TaskAtlasNode> fetchNodesByName0( String name );

    @Override
    default List<GraphNode> fetchNodesByName( String name ) {
        List<TaskAtlasNode> taskAtlasNodes = this.fetchNodesByName0(name);
        return new ArrayList<>(taskAtlasNodes);
    }

    @Override
    @Update("UPDATE `hydra_atlas_vgraph_nodes` SET `node_name` = #{nodeName}, `node_description` = #{nodeDescription} WHERE `guid` = #{guid}")
    void updateNode( GraphNode graphNode );
}
