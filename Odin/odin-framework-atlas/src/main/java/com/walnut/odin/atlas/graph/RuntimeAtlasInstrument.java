package com.walnut.odin.atlas.graph;

import java.util.List;

import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.vgraph.AtlasInstrument;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.slime.meta.TableIndexMeta;

import com.walnut.odin.atlas.advance.GraphStratumTape;

/**
 *  Pinecone Ursus For Java RuntimeAtlas
 *  Author: Ken, Harald.E (Dragon King)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Runtime Orchestration Atlas
 *  统一大规模运行矢量调度云图
 *  *****************************************************************************************
 */
public interface RuntimeAtlasInstrument extends Pinenut, AtlasInstrument {

    TaskInstrument taskInstrument();

    GraphNode queryGraphNodeByTaskGuid( GUID taskGuid );

    TaskElement queryTaskElementByGuid( GUID graphNodeGuid );

    GraphStratumTape tapedGraphStratumAdvancer(VectorDAG vectorDAG, KOIMappingDriver driver );

    String querySegmentName( GUID vgraphGuid, short stratumId, short runtimePriority );

    int countStratum( GUID vgraphGuid );

    int countPriority( GUID vgraphGuid, short stratumId );

    void putStratumMeta( GUID vgraphGuid, short stratumId, short runtimePriority, String segmentName );

    VectorDAG getByLayerGuid( GUID layerGuid );

    VectorDAG queryByPath( String path );



    @Unsafe( "TestOnly" )
    List<GraphNode> fetchIsolatedNodesAll();

    List<GraphNode> fetchIsolatedNodes( long offset, long limit );

    List<GraphNode> fetchIsolatedNodesById( long idStart, long idEnd );

    TableIndexMeta getIsolatedNodeIndexMeta();

    long queryMaxIsolatedNodePage( long limit );

}
