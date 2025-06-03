package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.List;

public abstract class ArchVectorDAG implements VectorDAG {
    protected List<GUID>                                mLstHandleNodeGuids;

    protected Layer                                     mGraphLayer;

    protected GUID                                      mLayerAffiDAGGuid;

    protected VectorGraphMasterManipulator              mMasterManipulator;

    protected VectorGraphManipulator                    mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator           mVectorGraphPathCacheManipulator;

    protected GuidAllocator                             mGuidAllocator;

    protected VectorGraphConfig                         mVectorGraphConfig;

    public ArchVectorDAG( @Nullable Layer affliatedLayer, List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this.mLstHandleNodeGuids                    = handleNodeGuids;
        this.mMasterManipulator                     = masterManipulator;
        this.mVectorGraphConfig                     = vectorGraphConfig;
        this.mVectorGraphManipulator                = this.mMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator       = this.mMasterManipulator.getVectorGraphPathCacheManipulator();
        this.mGuidAllocator                         = GUIDs.newGuidAllocator();
        this.mGraphLayer                            = affliatedLayer;

        if ( this.mGraphLayer != null ) {
            this.mLayerAffiDAGGuid = this.mGraphLayer.getGuid();
        }
    }

    // Temporary Graph
    public ArchVectorDAG( @Nullable GUID graphGuid, List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this( (Layer) null, handleNodeGuids, masterManipulator, vectorGraphConfig );

        if ( graphGuid == null ) {
            graphGuid = this.mGuidAllocator.nextGUID();
        }
        this.mLayerAffiDAGGuid = graphGuid;
    }

    // Temporary Graph
    public ArchVectorDAG( List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this( (GUID) null, handleNodeGuids, masterManipulator, vectorGraphConfig );
    }


    @Override
    public List<GUID> fetchSourceGuids(long offset, long limit ) {
        if( this.mLstHandleNodeGuids == null || this.mLstHandleNodeGuids.isEmpty() ) {
            return this.mVectorGraphManipulator.fetchHandleGuids(offset, limit);
        }
        else {
            return this.mLstHandleNodeGuids.subList( (int) offset, (int) (offset+limit) );
        }
    }

    @Override
    public List<GUID> fetchSourceGuidsByTaskPriority(long offset, long limit ) {
        if( this.mLstHandleNodeGuids == null || this.mLstHandleNodeGuids.isEmpty() ) {
            return this.mVectorGraphManipulator.fetchHandleGuidsByTaskPriority(offset, limit);
        }
        else {
            return this.mLstHandleNodeGuids.subList( (int) offset, (int) (offset+limit) );
        }
    }

    @Override
    public long countSourceNodes() {
        return this.mVectorGraphManipulator.countSourceNodes();
    }

    @Override
    public List<GUID> fetchDownstreamNodeGuid( GUID nodeGuid, long offset, long limit ) {
        return this.mVectorGraphManipulator.fetchDownstreamNodeGuid(nodeGuid,offset,limit);
    }

    @Override
    public List<GUID> fetchUpstreamNodeGuid( GUID nodeGuid, long offset, long limit ) {
        return this.mVectorGraphManipulator.fetchUpstreamNodeGuid(nodeGuid,offset,limit);
    }

    @Override
    public long queryInDegree( GUID nodeGuid ) {
        return this.mVectorGraphManipulator.queryInDegree(nodeGuid);
    }

    @Override
    public long queryOutDegree( GUID nodeGuid ) {
        return this.mVectorGraphManipulator.queryOutDegree(nodeGuid);
    }

    @Override
    public GraphNode get( GUID guid ) {
        return this.mVectorGraphManipulator.queryNode( guid );
    }

    @Override
    public void removeNode( GUID guid ) {
        this.mVectorGraphManipulator.removeNode( guid );
        this.mVectorGraphPathCacheManipulator.remove( guid );
    }

    @Override
    public GUID getAffiliateLayerGuid() {
        return this.mLayerAffiDAGGuid;
    }

    @Override
    public Layer getAffiliateLayer() {
        return this.mGraphLayer;
    }

    @Override
    public void addSourceNodeGuid(GUID handleNodeGuid ) {
        this.mLstHandleNodeGuids.add(handleNodeGuid);
    }

    @Override
    public VectorGraphConfig getConfig() {
        return this.mVectorGraphConfig;
    }

    @Override
    public Layer persistenceAsLayer( LayerInstrument layerInstrument, String name ) {
        TreeNode node = layerInstrument.get( this.mLayerAffiDAGGuid );
        if ( node == null ) {
            this.mGraphLayer = new AtlasLayer();
            this.mGraphLayer.setGuid( this.mLayerAffiDAGGuid );
        }

        this.mGraphLayer.setSourceGuids( this.mLstHandleNodeGuids );
        this.mGraphLayer.setName( name );

        if ( node == null ) {
            layerInstrument.put( this.mGraphLayer );
        }
        else {
            layerInstrument.update( this.mGraphLayer );
        }

        return this.mGraphLayer;
    }

    @Override
    public List<GraphNode> fetchChildNodes( GUID guid ) {
        return this.mVectorGraphManipulator.fetchChildNodes( guid );
    }

    @Override
    public List<GUID> fetchChildNodeGuids(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodeGuids( guid );
    }

    @Override
    public List<GUID> fetchChildNodeGuids(long offset, long limit, GUID guid) {
        return this.mVectorGraphManipulator.limitFetchChildNodeGuids( offset, limit, guid );
    }

    @Override
    public long countChildNodeNum(GUID guid) {
        return this.mVectorGraphManipulator.countChildNodeNums( guid );
    }

    @Override
    public long getPriorityByInDegree(GUID guid) {
        return this.mVectorGraphManipulator.getPriorityByInDegree( guid );
    }

    @Override
    public void addChild(GUID parentGuid, GUID childGuid) {
        this.mVectorGraphManipulator.addChild( parentGuid,childGuid );
    }
}
