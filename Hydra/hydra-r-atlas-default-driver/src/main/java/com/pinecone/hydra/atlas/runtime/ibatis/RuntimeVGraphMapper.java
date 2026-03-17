package com.pinecone.hydra.atlas.runtime.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.graph.entity.TaskAtlasNode;
import com.pinecone.hydra.atlas.graph.entity.TaskGraphNode;
import com.pinecone.hydra.atlas.graph.source.TaskGraphManipulator;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.meta.TableIndex64Meta;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@SuppressWarnings("unchecked")
@IbatisDataAccessObject
public interface RuntimeVGraphMapper extends TaskGraphManipulator {
    @Override
    default void insertHandleNode( GraphNode graphNode ){
        this.insertGraphNode(graphNode);
    }

    @Insert("INSERT INTO `hydra_atlas_vgraph_nodes` " +
            "(`guid`, `task_guid`, `node_name`, `node_description`) " +
            "VALUES " +
            "(#{graphNode.guid}, #{graphNode.taskGuid}, #{graphNode.name}, #{graphNode.description})"
    )
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
    @Select("SELECT " +
            "    `id` AS enumId, " +
            "    `guid`, " +
            "    `task_guid`, " +
            "    `node_name` AS name, " +
            "    `node_description` AS description " +
            "FROM `hydra_atlas_vgraph_nodes` " +
            "WHERE `guid` = #{guid}")
    TaskAtlasNode queryNode( @Param("guid") GUID guid );

    @Override
    @Select("SELECT " +
            "    `id` AS enumId, " +
            "    `guid`, " +
            "    `task_guid`, " +
            "    `node_name` AS name, " +
            "    `node_description` AS description " +
            "FROM `hydra_atlas_vgraph_nodes` " +
            "WHERE `task_guid` = #{taskGuid}")
    TaskGraphNode getNodeByTaskGuid( @Param("taskGuid") GUID taskGuid );

    @Override
    @Select("SELECT `task_guid` FROM `hydra_atlas_vgraph_nodes` WHERE `guid` = #{nodeId}")
    GUID queryTaskGuidByNodeId( GUID nodeId );

    @Override
    @Select("SELECT `parent_guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `guid` = #{guid}")
    List<GUID> fetchParentIds( @Param("guid") GUID guid );

    @Select("SELECT " +
            "    havn.`id`, " +
            "    havn.`guid`, " +
            "    havn.`task_guid`, " +
            "    havn.`node_name`, " +
            "    havn.`node_description` " +
            "FROM `hydra_atlas_vgraph_nodes` havn " +
            "JOIN `hydra_atlas_vgraph_adjacent` hava " +
            "    ON havn.`guid` = hava.`guid` " +
            "WHERE hava.`parent_guid` = #{guid}")
    List<TaskAtlasNode> fetchChildNodes0( @Param("guid") GUID guid );

    @Override
    default List<GraphNode> fetchChildNodes( GUID guid ) {
        return (List) this.fetchChildNodes0( guid );
    }

    @Override
    @Select("SELECT havn.`guid` FROM `hydra_atlas_vgraph_nodes` havn, `hydra_atlas_vgraph_adjacent` hava WHERE hava.`parent_guid` = #{guid} ")
    List<GUID> fetchChildNodeGuids(GUID guid);


    @Override
    default List<GraphNode> fetchRootNodes() {
        return (List) this.fetchRootNodes0();
    }

    @Select("SELECT " +
            "    havn.`id`, " +
            "    havn.`guid`, " +
            "    havn.`task_guid`, " +
            "    havn.`node_name`, " +
            "    havn.`node_description` " +
            "FROM hydra_atlas_vgraph_nodes havn " +
            "WHERE NOT EXISTS ( " +
            "    SELECT 1 " +
            "    FROM hydra_atlas_vgraph_adjacent hava " +
            "    WHERE hava.`guid` = havn.`guid` " +
            ")")
    List<TaskAtlasNode> fetchRootNodes0();

    @Override
    @Select("SELECT `guid` FROM `hydra_atlas_vgraph_adjacent` WHERE `parent_guid` = #{parentGuid}")
    List<GUID> fetchChildNodeIds( @Param("guid") GUID guid );


    @Select("SELECT " +
            "    `id` AS enumId, " +
            "    `guid`, " +
            "    `task_guid`, " +
            "    `node_name` AS name, " +
            "    `node_description` AS description " +
            "FROM `hydra_atlas_vgraph_nodes` " +
            "WHERE `node_name` = #{name}")
    List<TaskAtlasNode> fetchNodesByName0(  @Param("name") String name );


    @Override
    default List<GraphNode> fetchNodesByName( String name ) {
        return (List) this.fetchNodesByName0( name );
    }

    @Override
    @Update("UPDATE `hydra_atlas_vgraph_nodes` " +
            "SET " +
            "    `task_guid` = #{graphNode.taskGuid}, " +
            "    `node_name` = #{graphNode.nodeName}, " +
            "    `node_description` = #{graphNode.nodeDescription} " +
            "WHERE `guid` = #{graphNode.guid}")
    void updateNode(  @Param("graphNode") GraphNode graphNode );

    @Override
    @Select("SELECT havn.guid " +
            "FROM `hydra_atlas_vgraph_nodes` havn " +
            "WHERE NOT EXISTS ( " +
            "    SELECT `id` " +
            "    FROM `hydra_atlas_vgraph_adjacent` hava " +
            "    WHERE hava.guid = havn.guid " +
            ") " +
            "LIMIT #{limit} OFFSET #{offset} LIMIT #{limit} OFFSET #{offset}")
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






    @Select(
        "SELECT " +
        "`id` AS enumId, " +
        "`guid` AS guid, " +
        "`task_guid` AS taskGuid, " +
        "`node_name` AS name, " +
        "`node_description` AS description, " +
        "`is_isolated` AS isolated, " +
        "`create_time` AS createTime, " +
        "`update_time` AS updateTime " +
        "FROM `hydra_atlas_vgraph_nodes` " +
        "WHERE `is_isolated` = 1 " +
        "ORDER BY `id` ASC " +
        "LIMIT #{limit} OFFSET #{offset}"
    )
    List<TaskAtlasNode> fetchIsolatedNodes0(
        @Param("offset") long offset,
        @Param("limit") long limit
    );

    @Override
    default List<GraphNode> fetchIsolatedNodes( long offset, long limit ) {
        return ( List ) this.fetchIsolatedNodes0( offset, limit );
    }


    @Select(
        "SELECT " +
        "`id` AS enumId, " +
        "`guid` AS guid, " +
        "`task_guid` AS taskGuid, " +
        "`node_name` AS name, " +
        "`node_description` AS description, " +
        "`is_isolated` AS isolated, " +
        "`create_time` AS createTime, " +
        "`update_time` AS updateTime " +
        "FROM `hydra_atlas_vgraph_nodes` " +
        "WHERE `is_isolated` = 1 " +
        "AND `id` >= #{idStart} AND `id` <= #{idEnd} " +
        "ORDER BY `id` ASC"
    )
    List<TaskAtlasNode> fetchIsolatedNodesById0(
        @Param("idStart") long idStart, @Param("idEnd") long idEnd
    );

    @Override
    default List<GraphNode> fetchIsolatedNodesById( long idStart, long idEnd ) {
        return ( List ) this.fetchIsolatedNodesById0( idStart, idEnd );
    }

    @Override
    @Select(
        "SELECT COUNT( * ) " +
        "FROM `hydra_atlas_vgraph_nodes` WHERE `is_isolated` = 1"
    )
    long countIsolatedNodes();

    @Override
    @Select(
        "SELECT " +
        "COALESCE( MIN(`id`), 0 ) AS minId, " +
        "COALESCE( MAX(`id`), 0 ) AS maxId " +
        "FROM `hydra_atlas_vgraph_nodes` " +
        "WHERE `is_isolated` = 1"
    )
    TableIndex64Meta selectIsolatedNodeIndexMeta();
}
