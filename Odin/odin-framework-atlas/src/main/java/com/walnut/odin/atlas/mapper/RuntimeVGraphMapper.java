package com.walnut.odin.atlas.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
    default void insertHandleNode( GraphNode graphNode ) {
        this.insertGraphNode( graphNode );
    }

    @Override
    void insertGraphNode( @Param("graphNode") GraphNode graphNode );

    void insertNodeAdjacent( @Param("parentGuid") GUID parentGuid, @Param("childGuid") GUID childGuid );

    @Override
    default void insertNodeByEdge( GUID parentGuid, GraphNode graphNode ) {
        this.insertGraphNode( graphNode );
        this.insertNodeAdjacent( parentGuid, graphNode.getId() );
        this.markNonSource( graphNode.getId() );
    }


    default void addChild( GUID parentGuid, GraphNode graphNode ) {
        this.insertNodeAdjacent( parentGuid, graphNode.getId() );
        this.markNonSource( graphNode.getId() );
    }

    @Override
    default void removeNode( GUID guid ) {
        this.removeGraphNode(guid);
        this.removeGraphAdjacent(guid);
    }

    void removeGraphNode(  @Param("guid") GUID guid );

    void removeGraphAdjacent(  @Param("guid") GUID guid );

    void markNonSource( @Param("guid") GUID guid );

    void affirmSourceIfNoParent( @Param("guid") GUID guid );

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
    List<GUID> fetchChildNodeGuids( GUID guid );


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
    void updateNode(  @Param("graphNode") GraphNode graphNode );

    @Override
    List<GUID> fetchHandleGuids( @Param("offset") long offset, @Param("limit") long limit);

    @Override
    List<GUID> fetchHandleGuidsByTaskPriority( @Param("offset") long offset, @Param("limit") long limit );

    @Override
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
    long getPriorityByInDegree( @Param("guid") GUID guid );


    @Override
    List<GUID> limitFetchChildNodeGuids( @Param("offset") long offset, @Param("limit") long limit, @Param("guid") GUID guid );

    @Override
    long countChildNodeNums( GUID guid );

    @Override
    default void addChild( GUID parentGuid, GUID childGuid ) {
        this.insertNodeAdjacent( parentGuid, childGuid );
        this.markNonSource( childGuid );
    }






    List<TaskAtlasNode> fetchSourceNodes0(
        @Param("offset") long offset,
        @Param("limit") long limit
    );

    @Override
    default List<GraphNode> fetchSourceNodes( long offset, long limit ) {
        return ( List ) this.fetchSourceNodes0( offset, limit );
    }


    List<TaskAtlasNode> fetchSourceNodesById0(
        @Param("idStart") long idStart, @Param("idEnd") long idEnd
    );

    @Override
    default List<GraphNode> fetchSourceNodesById( long idStart, long idEnd ) {
        return ( List ) this.fetchSourceNodesById0( idStart, idEnd );
    }

    @Override
    TableIndex64Meta selectSourceNodeIndexMeta();

}
