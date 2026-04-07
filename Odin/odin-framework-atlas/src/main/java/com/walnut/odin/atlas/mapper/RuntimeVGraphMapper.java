package com.walnut.odin.atlas.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.pinecone.slime.meta.TableIndex64Meta;

import com.walnut.odin.atlas.graph.entity.TaskAtlasNode;
import com.walnut.odin.atlas.graph.entity.TaskGraphNode;

@SuppressWarnings("unchecked")
@IbatisDataAccessObject
public interface RuntimeVGraphMapper extends TaskGraphManipulator {
    @Override
    default void insertHandleNode( GraphNode graphNode ){
        this.insertGraphNode(graphNode);
    }

    @Override
    void insertGraphNode( @Param("graphNode") GraphNode graphNode );

    void insertNodeAdjacent( @Param("parentGuid") GUID parentGuid, @Param("childGuid") GUID childGuid );

    @Override
    default void insertNodeByEdge(GUID parentGuid, GraphNode graphNode){
        this.insertGraphNode(graphNode);
        this.insertNodeAdjacent(parentGuid,graphNode.getId());
    }


    default void addChild(GUID parentGuid, GraphNode graphNode) {
        this.insertNodeAdjacent(parentGuid,graphNode.getId());
    }

    @Override
    default void removeNode( GUID guid ) {
        this.removeGraphNode(guid);
        this.removeGraphAdjacent(guid);
    }

    void removeGraphNode(  @Param("guid") GUID guid );

    void removeGraphAdjacent(  @Param("guid") GUID guid );

    @Override
    TaskAtlasNode queryNode( @Param("guid") GUID guid );

    @Override
    TaskGraphNode getNodeByTaskGuid( @Param("taskGuid") GUID taskGuid );

    @Override
    GUID queryTaskGuidByNodeId( GUID nodeId );

    @Override
    List<GUID> fetchParentIds( @Param("guid") GUID guid );

    List<TaskAtlasNode> fetchChildNodes0( @Param("guid") GUID guid );

    @Override
    default List<GraphNode> fetchChildNodes( GUID guid ) {
        return (List) this.fetchChildNodes0( guid );
    }

    @Override
    List<GUID> fetchChildNodeGuids(GUID guid);


    @Override
    default List<GraphNode> fetchRootNodes() {
        return (List) this.fetchRootNodes0();
    }

    List<TaskAtlasNode> fetchRootNodes0();

    @Override
    List<GUID> fetchChildNodeIds( @Param("guid") GUID guid );

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
    List<GUID> fetchDownstreamNodeGuid( @Param("nodeGuid") GUID nodeGuid, @Param("offset") long offset, @Param("limit") long limit);

    @Override
    List<GUID> fetchUpstreamNodeGuid( @Param("nodeGuid") GUID nodeGuid, @Param("offset") long offset, @Param("limit") long limit);

    @Override
    long queryInDegree( @Param("nodeGuid") GUID nodeGuid);

    @Override
    long queryOutDegree( @Param("nodeGuid") GUID nodeGuid);

    @Override
    long getPriorityByInDegree(@Param("guid") GUID guid);


    @Override
    List<GUID> limitFetchChildNodeGuids(@Param("offset") long offset, @Param("limit") long limit, @Param("guid") GUID guid);

    @Override
    long countChildNodeNums(GUID guid);

    @Override
    void addChild(GUID parentGuid, GUID childGuid);






    List<TaskAtlasNode> fetchIsolatedNodes0(
        @Param("offset") long offset,
        @Param("limit") long limit
    );

    @Override
    default List<GraphNode> fetchIsolatedNodes( long offset, long limit ) {
        return ( List ) this.fetchIsolatedNodes0( offset, limit );
    }


    List<TaskAtlasNode> fetchIsolatedNodesById0(
        @Param("idStart") long idStart, @Param("idEnd") long idEnd
    );

    @Override
    default List<GraphNode> fetchIsolatedNodesById( long idStart, long idEnd ) {
        return ( List ) this.fetchIsolatedNodesById0( idStart, idEnd );
    }

    @Override
    long countIsolatedNodes();

    @Override
    TableIndex64Meta selectIsolatedNodeIndexMeta();

}
