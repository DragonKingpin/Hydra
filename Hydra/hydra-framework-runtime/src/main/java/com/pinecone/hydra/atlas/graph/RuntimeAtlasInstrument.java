package com.pinecone.hydra.atlas.graph;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.advance.GraphStratumTape;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.vgraph.AtlasInstrument;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

/**
 *  Pinecone Ursus For Java RuntimeAtlas
 *  Author: Ken, Harold.E (Dragon King)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Runtime Orchestration Atlas
 *  统一大规模运行矢量调度云图
 *  *****************************************************************************************
 */
public interface RuntimeAtlasInstrument extends Pinenut, AtlasInstrument {
    GUID put(GraphNode graphNode, GUID taskGuid );

    GraphNode queryGraphNodeByTaskGuid( GUID taskGuid );

    TaskElement queryTaskElementByGuid(GUID graphNodeGuid );

    GraphStratumTape tapedGraphStratumAdvancer(VectorDAG vectorDAG, KOIMappingDriver driver );

    String querySegmentName( GUID vgraphGuid, short stratumId, short runtimePriority );

    int countStratum( GUID vgraphGuid );

    int countPriority( GUID vgraphGuid, short stratumId );

    void putStratumMeta( GUID vgraphGuid, short stratumId, short runtimePriority, String segmentName );

}
