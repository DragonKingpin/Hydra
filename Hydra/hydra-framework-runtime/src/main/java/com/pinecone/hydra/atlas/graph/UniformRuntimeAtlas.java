package com.pinecone.hydra.atlas.graph;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.advance.GenericGraphStratumTape;
import com.pinecone.hydra.atlas.advance.GraphStratumTape;
import com.pinecone.hydra.atlas.graph.entity.TaskGraphNode;
import com.pinecone.hydra.atlas.graph.source.QueueStratumManipulator;
import com.pinecone.hydra.atlas.graph.source.RuntimeMasterManipulator;
import com.pinecone.hydra.atlas.graph.source.VgraphTaskMappingManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.entity.TaskTreeNode;
import com.pinecone.hydra.unit.vgraph.GenericClosedVectorDAG;
import com.pinecone.hydra.unit.vgraph.GenericVectorDAG;
import com.pinecone.hydra.unit.vgraph.ArchAtlasInstrument;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.VectorGraphConfig;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;

import java.util.List;

public class UniformRuntimeAtlas extends ArchAtlasInstrument implements RuntimeAtlasInstrument {

    private TaskInstrument                  mTaskInstrument;

    private RuntimeMasterManipulator        mRuntimeMasterManipulator;

    private VgraphTaskMappingManipulator    mVgraphTaskMappingManipulator;

    private QueueStratumManipulator         mQueueStratumManipulator;

    public UniformRuntimeAtlas( List<GraphNode> parent,TaskInstrument taskInstrument, AtlasMappingDriver driver, VectorGraphConfig config ) {
        super(parent,driver,config);
        this.mTaskInstrument = taskInstrument;
        this.mRuntimeMasterManipulator = (RuntimeMasterManipulator) this.mAtlasMasterManipulator;
        this.mVgraphTaskMappingManipulator = this.mRuntimeMasterManipulator.getVgraphTaskMappingManipulator();
        this.mQueueStratumManipulator = this.mRuntimeMasterManipulator.getQueueStratumManipulator();
    }

    public UniformRuntimeAtlas( AtlasMappingDriver driver, TaskInstrument taskInstrument ) {
        super(driver);
        this.mTaskInstrument = taskInstrument;
        this.mRuntimeMasterManipulator = (RuntimeMasterManipulator) this.mAtlasMasterManipulator;
        this.mVgraphTaskMappingManipulator = this.mRuntimeMasterManipulator.getVgraphTaskMappingManipulator();
        this.mQueueStratumManipulator = this.mRuntimeMasterManipulator.getQueueStratumManipulator();
    }

    public GUID put( GraphNode graphNode ) {
        return super.put(graphNode);
    }

    public void remove( GUID guid ) {
        super.remove(guid);
    }

    public TaskGraphNode query( GUID guid ) {
        return (TaskGraphNode) super.get(guid);
    }

    @Override
    public GUID putMappingTask( GraphNode graphNode, GUID TaskGuid ) {
        GUID guid = this.put(graphNode);
        this.mVgraphTaskMappingManipulator.insert( TaskGuid, guid );
        return guid;
    }

    @Override
    public GraphNode queryGraphNodeByTaskGuid( GUID taskGuid ) {
        GUID guid = this.mVgraphTaskMappingManipulator.queryVgraphNodeGuid( taskGuid );
        return this.query(guid);
    }

    @Override
    public TaskElement queryTaskElementByGuid( GUID graphNodeGuid ) {
        GUID guid = this.mVgraphTaskMappingManipulator.queryTaskGuid(graphNodeGuid);
        TaskTreeNode taskTreeNode = (TaskTreeNode) this.mTaskInstrument.get( guid );
        ElementNode elementNode = taskTreeNode.evinceElementNode();
        if ( elementNode != null ) {
            return elementNode.evinceTaskElement();
        }
        return null;
    }

    @Override
    public GraphStratumTape tapedGraphStratumAdvancer(VectorDAG vectorDAG, KOIMappingDriver driver) {
        return new GenericGraphStratumTape( this, vectorDAG, driver );
    }

    @Override
    public String querySegmentName(GUID vgraphGuid, short stratumId, short runtimePriority) {
        return this.mQueueStratumManipulator.querySegmentName( vgraphGuid, stratumId, runtimePriority );
    }

    @Override
    public int countStratum(GUID vgraphGuid) {
        return this.mQueueStratumManipulator.countStratum( vgraphGuid );
    }

    @Override
    public int countPriority(GUID vgraphGuid, short stratumId) {
        return this.mQueueStratumManipulator.countPriority( vgraphGuid, stratumId );
    }

    @Override
    public void putStratumMeta(GUID vgraphGuid, short stratumId, short runtimePriority, String segmentName) {
        this.mQueueStratumManipulator.put( vgraphGuid, stratumId, runtimePriority, segmentName );
    }

    @Override
    public VectorDAG toVectorDAG( Layer layer ) {
        List<GUID> sourceGuids = layer.getSourceGuids();
        List<GUID> sinkGuids = layer.getSinkGuids();
        return new GenericClosedVectorDAG( layer.getGuid(), sourceGuids,sinkGuids, this.mMegaVectorDAG.getMasterManipulator(), this.mMegaVectorDAG.getConfig() );
    }

    @Override
    public void addChild(GUID parentGuid, GUID childGuid) {
        this.mMegaVectorDAG.addChild( parentGuid,childGuid );
    }
}
