package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.LayerManager;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.util.List;

public abstract class ArchVectorDAG implements VectorDAG {
    protected List<GUID>                                mLstHandleNodeGuids;

    protected VectorGraphMasterManipulator              mMasterManipulator;

    protected VectorGraphManipulator                    mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator           mVectorGraphPathCacheManipulator;

    protected GuidAllocator                             mGuidAllocator;

    protected VectorGraphConfig                         mVectorGraphConfig;

    public ArchVectorDAG( List<GUID> handleNodeGuids, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig) {
        this.mLstHandleNodeGuids                    = handleNodeGuids;
        this.mMasterManipulator                     = masterManipulator;
        this.mVectorGraphConfig                     = vectorGraphConfig;
        this.mVectorGraphManipulator                = this.mMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator       = this.mMasterManipulator.getVectorGraphPathCacheManipulator();
        this.mGuidAllocator                         = new GenericGuidAllocator();
    }
    @Override
    public List<GUID> fetchHandleGuids(long offset, long limit) {
        return this.mVectorGraphManipulator.fetchHandleGuids(offset, limit);
    }

    @Override
    public long countHandleNodes() {
        return this.mVectorGraphManipulator.countHandleNodes();
    }

    @Override
    public List<GUID> fetchDownstreamNodeGuid(GUID nodeGuid, long offset, long limit) {
        return this.mVectorGraphManipulator.fetchDownstreamNodeGuid(nodeGuid,offset,limit);
    }

    @Override
    public List<GUID> fetchUpstreamNodeGuid(GUID nodeGuid, long offset, long limit) {
        return this.mVectorGraphManipulator.fetchUpstreamNodeGuid(nodeGuid,offset,limit);
    }

    @Override
    public long queryInDegree(GUID nodeGuid) {
        return this.mVectorGraphManipulator.queryInDegree(nodeGuid);
    }

    @Override
    public long queryOutDegree(GUID nodeGuid) {
        return this.mVectorGraphManipulator.queryOutDegree(nodeGuid);
    }

    @Override
    public void saveVectorDAG(VectorDAG vectorDAG) {

    }

    @Override
    public void addHandleNodeGuid(GUID handleNodeGuid) {
        this.mLstHandleNodeGuids.add(handleNodeGuid);
    }

    @Override
    public VectorGraphConfig getConfig() {
        return this.mVectorGraphConfig;
    }

    @Override
    public void save(LayerManager layerManager, String name) {
        AtlasLayer atlasLayer = new AtlasLayer();
        atlasLayer.setHandleGuids( this.mLstHandleNodeGuids );
        atlasLayer.setName( name );
        layerManager.put( atlasLayer );
    }
}
