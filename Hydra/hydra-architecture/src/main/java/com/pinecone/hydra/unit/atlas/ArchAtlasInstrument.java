package com.pinecone.hydra.unit.atlas;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.kom.PathSelector;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.MagnitudeVectorDAG;
import com.pinecone.hydra.unit.vgraph.MegaVectorDAG;
import com.pinecone.hydra.unit.vgraph.VectorGraphConfig;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;
import lombok.val;

import java.util.List;

public abstract class ArchAtlasInstrument implements AtlasInstrument{
    protected MegaVectorDAG                     mMegaVectorDAG;

    protected AtlasInstrument                   mParentInstrument;

    protected Hydrarum                          mHydrarum;

    protected Processum                         mSuperiorProcess;

    protected GuidAllocator                     mGuidAllocator;

    protected PathResolver                      mPathResolver;

    protected PathSelector                      mPathSelector;

    protected VectorGraphMasterManipulator      mMasterManipulator;

    protected VectorGraphManipulator            mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator   mVectorGraphPathCacheManipulator;

    protected VectorGraphConfig                 mVectorGraphConfig;

    public ArchAtlasInstrument (
            Processum superiorProcess, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig,
            AtlasInstrument parent, String name
    ){
        this.mMasterManipulator = masterManipulator;
        this.mVectorGraphConfig = vectorGraphConfig;
        this.mMegaVectorDAG = new MagnitudeVectorDAG( this.mMasterManipulator, this.mVectorGraphConfig );
        this.mSuperiorProcess = superiorProcess;
        if ( this.mSuperiorProcess instanceof Hydrarum ) {
            this.mHydrarum                    = (Hydrarum) this.mSuperiorProcess;
        }
        else {
            this.mHydrarum                    = (Hydrarum) superiorProcess.getSystem();
        }
        this.mParentInstrument = parent;

        this.mGuidAllocator = new GenericGuidAllocator();
        this.mVectorGraphManipulator = this.mMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator = this.mMasterManipulator.getVectorGraphPathCacheManipulator();

    }

    @Override
    public AtlasInstrument parent() {
        return this.mParentInstrument;
    }

    @Override
    public Processum getSuperiorProcess() {
        return this.mSuperiorProcess;
    }

    @Override
    public void setParent(AtlasInstrument atlasInstrument) {
        this.mParentInstrument = atlasInstrument;
    }

    @Override
    public String getPath(GUID guid) {
        return null;
    }

    @Override
    public GUID queryGUIDByPath(String path) {
        return null;
    }

    @Override
    public boolean contains(GUID nodeGuid) {
        return false;
    }

    @Override
    public GUID put(GraphNode graphNode) {
        return null;
    }

    @Override
    public GraphNode get(GUID guid) {
        return null;
    }

    @Override
    public GUID queryGUIDByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public TreeNode get(GUID guid, int depth) {
        return null;
    }

    @Override
    public TreeNode getSelf(GUID guid) {
        return null;
    }

    @Override
    public void remove(GUID guid) {

    }

    @Override
    public void remove(String path) {

    }

    @Override
    public List<GraphNode> getChildren(GUID guid) {
        return null;
    }

    @Override
    public List<GUID> fetchChildrenGuids(GUID guid) {
        return null;
    }

    @Override
    public void rename(GUID guid, String name) {

    }
}
