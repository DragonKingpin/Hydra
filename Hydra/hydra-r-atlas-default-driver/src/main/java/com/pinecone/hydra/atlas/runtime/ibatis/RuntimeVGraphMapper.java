package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.entity.TaskAtlasNode;
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


@IbatisDataAccessObject
public interface RuntimeVGraphMapper extends VectorGraphManipulator {
    @Override
    default void insertHandleNode( GraphNode graphNode ){
        this.insertGraphNode(graphNode);
    }

    @Insert("INSERT INTO `hydra_atlas_vgraph_nodes` (`guid`, `node_name`, `node_description`) VALUES (#{graphNode.guid},#{graphNode.name},#{graphNode.description})")
    void insertGraphNode(  @Param("graphNode") GraphNode graphNode );

    @Insert("INSERT INTO `hydra_atlas_vgraph_adjacent` (`guid`, `linked_type`, `parent_guid`) VALUES (#{childGuid},'weak',#{parentGuid})")
    void insertNodeAdjacent( @Param("parentGuid") GUID parentGuid, @Param("childGuid") GUID childGuid );

    @Override
    default void insertNodeByEdge(GUID parentGuid, GraphNode graphNode){
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
    void removeGraphNode(  @Param("guid") GUID guid );

    @Delete("DELETE FROM `hydra_atlas_vgraph_adjacent` WHERE `guid` = #{guid}")
    void removeGraphAdjacent(  @Param("guid") GUID guid );

    @Override
    @Select("SELECT `id` AS enumId, `guid`, `node_name` AS name, `node_description` AS description  FROM `hydra_atlas_vgraph_nodes` WHERE `guid` = #{guid}")
    TaskAtlasNode queryNode( @Param("guid") GUID guid );

    @Override
    @Select("SELECT `parent_guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `guid` = #{guid}")
    List<GUID> fetchParentIds( @Param("guid") GUID guid );

    @Override
    @Select("SELECT havn.`id`,havn.`guid`,havn.`node_name`,havn.`node_description` FROM `hydra_atlas_vgraph_nodes` havn, `hydra_atlas_vgraph_adjacent` hava WHERE hava.`parent_guid` = #{guid} ")
    List<GraphNode> fetchChildNodes(  @Param("guid") GUID guid );

    @Override
    @Select("SELECT havn.`guid` FROM `hydra_atlas_vgraph_nodes` havn, `hydra_atlas_vgraph_adjacent` hava WHERE hava.`parent_guid` = #{guid} ")
    List<GUID> fetchChildNodeGuids(GUID guid);

    @Override
    @Select("SELECT \n" +
            "    havn.`id`,\n" +
            "    havn.`guid`,\n" +
            "    havn.`node_name`,\n" +
            "    havn.`node_description`\n," +
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
    List<GUID> fetchChildNodeIds( @Param("guid") GUID guid );


    @Select("SELECT `id` AS enumId, `guid`, `node_name` AS name, `node_description` AS description  FROM `hydra_atlas_vgraph_nodes` WHERE `node_name` = #{name}")
    List<TaskAtlasNode> fetchNodesByName0(  @Param("name") String name );

    @Override
    default List<GraphNode> fetchNodesByName( String name ) {
        List<TaskAtlasNode> taskAtlasNodes = this.fetchNodesByName0(name);
        return new ArrayList<>(taskAtlasNodes);
    }

    @Override
    @Update("UPDATE `hydra_atlas_vgraph_nodes` SET `node_name` = #{graphNode.nodeName}, `node_description` = #{graphNode.nodeDescription} WHERE `guid` = #{graphNode.guid}")
    void updateNode(  @Param("graphNode") GraphNode graphNode );

    @Override
    @Select("SELECT `havn`.guid " +
            "FROM `hydra_atlas_vgraph_nodes` havn" +
            "WHERE NOT EXISTS (SELECT `id` FROM `hydra_atlas_vgraph_adjacent` hava WHERE `hava`.guid = `havn`.guid) " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<GUID> fetchHandleGuids( @Param("offset") long offset, @Param("limit") long limit);

    @Override
    @Select("SELECT havn.guid " +
            "FROM hydra_atlas_vgraph_nodes havn " +
            "JOIN hydra_atlas_vgraph_task_mapping vatm ON havn.guid = vatm.vgraph_node_guid " +
            "JOIN hydra_task_task_node httn ON vatm.task_guid = httn.guid " +
            "WHERE NOT EXISTS (" +
            "SELECT id FROM hydra_atlas_vgraph_adjacent hava WHERE hava.guid = havn.guid) " +
            "ORDER BY httn.priority " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<GUID> fetchHandleGuidsByTaskPriority(long offset, long limit);

    @Override
    @Select("SELECT COUNT(havn.guid) " +
            "FROM `hydra_atlas_vgraph_nodes` havn " +
            "WHERE NOT EXISTS (SELECT `id` FROM `hydra_atlas_vgraph_adjacent` `hava` WHERE `hava`.guid = `havn`.guid)")
    long countSourceNodes();

    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `parent_guid` = #{nodeGuid} LIMIT #{limit} OFFSET #{offset}")
    List<GUID> fetchDownstreamNodeGuid( @Param("nodeGuid") GUID nodeGuid, @Param("offset") long offset, @Param("limit") long limit);

    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `parent_guid` = #{nodeGuid} LIMIT #{limit} OFFSET #{offset}")
    List<GUID> fetchUpstreamNodeGuid( @Param("nodeGuid") GUID nodeGuid, @Param("offset") long offset, @Param("limit") long limit);

    @Override
    @Select("SELECT COUNT(`guid`) FROM `hydra_atlas_vgraph_adjacent` WHERE `parent_guid` = #{nodeGuid}")
    long queryInDegree( @Param("nodeGuid") GUID nodeGuid);

    @Override
    @Select("SELECT COUNT(`guid`) FROM `hydra_atlas_vgraph_adjacent` WHERE `guid` = #{nodeGuid}")
    long queryOutDegree( @Param("nodeGuid") GUID nodeGuid);

    @Override
    @Select("SELECT COUNT(*) + 1 " +
            "FROM (" +
            "    SELECT guid, COUNT(*) AS degree " +
            "    FROM hydra_atlas_vgraph_adjacent " +
            "    GROUP BY guid" +
            ") t1 " +
            "WHERE t1.degree > (" +
            "    SELECT COUNT(*) " +
            "    FROM hydra_atlas_vgraph_adjacent " +
            "    WHERE guid = #{guid}" +
            ")")
    long getPriorityByInDegree(@Param("guid") GUID guid);


    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_adjacent`  WHERE `parent_guid` = #{guid} LIMIT #{limit} OFFSET #{offset} ")
    List<GUID> limitFetchChildNodeGuids(@Param("offset") long offset, @Param("limit") long limit, @Param("guid") GUID guid);

    @Override
    @Select("SELECT COUNT(`id`) FROM hydra_atlas_vgraph_adjacent WHERE parent_guid = #{guid}")
    long countChildNodeNums(GUID guid);

    @Override
    @Insert("INSERT INTO `hydra_atlas_vgraph_adjacent` (guid, linked_type, parent_guid) VALUES (#{childGuid},'weak',#{parentGuid})")
    void addChild(GUID parentGuid, GUID childGuid);
}
