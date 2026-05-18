package com.pinecone.hydra.unit.vgraph;

import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;

public class MagnitudeVectorDAG extends ArchVectorDAG implements VectorDAG {
    protected Layer                                     mGraphLayer;

    protected VectorGraphMasterManipulator              mMasterManipulator;
    protected VectorGraphManipulator                    mVectorGraphManipulator;

    public MagnitudeVectorDAG( Layer affliatedLayer, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig ) {
        super( affliatedLayer.getGuid(), vectorGraphConfig );

        this.mGraphLayer                            = affliatedLayer;
        this.mMasterManipulator                     = masterManipulator;
        this.mVectorGraphManipulator                = this.mMasterManipulator.getVectorGraphManipulator();
    }

    @Override
    public GUID getAffiliateLayerGuid() {
        return this.mGraphLayer.getGuid();
    }

    @Override
    public Layer getAffiliateLayer() {
        return this.mGraphLayer;
    }

    @Override
    public boolean isPersistenceGraph() {
        return true;
    }

    @Override
    public List<GUID> fetchSourceGuids( long offset, long limit ) {
        return this.mVectorGraphManipulator.fetchHandleGuids( offset, limit );
    }

    @Override
    public List<GUID> fetchSourceGuidsByTaskPriority( long offset, long limit ) {
        return this.mVectorGraphManipulator.fetchHandleGuidsByTaskPriority(offset, limit);
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
    }

    @Override
    public List<GraphNode> fetchChildNodes( GUID guid ) {
        return this.mVectorGraphManipulator.fetchChildNodes( guid );
    }

    @Override
    public List<GUID> fetchChildNodeGuids( GUID guid ) {
        return this.mVectorGraphManipulator.fetchChildNodeGuids( guid );
    }

    @Override
    public List<GUID> fetchChildNodeGuids( long offset, long limit, GUID guid ) {
        return this.mVectorGraphManipulator.limitFetchChildNodeGuids( offset, limit, guid );
    }

    @Override
    public long countChildNodeNum( GUID guid ) {
        return this.mVectorGraphManipulator.countChildNodeNums( guid );
    }

    @Override
    public long getPriorityByInDegree( GUID guid ) {
        return this.mVectorGraphManipulator.getPriorityByInDegree( guid );
    }

    @Override
    public void addChild( GUID parentGuid, GUID childGuid ) {
        this.mVectorGraphManipulator.addChild( parentGuid,childGuid );
    }

}
