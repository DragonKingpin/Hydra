package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.unit.vgraph.algo.BasicDAGPathResolver;
import com.pinecone.hydra.unit.vgraph.algo.BasicDAGPathSelector;
import com.pinecone.hydra.unit.vgraph.algo.DAGPathResolver;
import com.pinecone.hydra.unit.vgraph.algo.DAGPathSelector;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.util.List;

public class MagnitudeVectorDAG implements MegaVectorDAG {
    protected List<GraphNode>                   lstHandles;
    protected VectorGraphMasterManipulator      mMasterManipulator;

    protected VectorGraphManipulator            mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator   mVectorGraphPathCacheManipulator;

    protected GuidAllocator                     mGuidAllocator;

    protected VectorGraphConfig                 mVectorGraphConfig;

    public MagnitudeVectorDAG(  List<GraphNode> handles, VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig){
        this.lstHandles                 = handles;
        this.mMasterManipulator = masterManipulator;
        this.mVectorGraphConfig = vectorGraphConfig;
        this.mVectorGraphManipulator = this.mMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator = this.mMasterManipulator.getVectorGraphPathCacheManipulator();
        this.mGuidAllocator = new GenericGuidAllocator();
    }

    @Override
    public VectorGraphMasterManipulator getMasterManipulator() {
        return this.mMasterManipulator;
    }

    @Override
    public GUID put(GraphNode graphNode) {
        GUID guid = this.mGuidAllocator.nextGUID();
        graphNode.setId( guid );
        this.mVectorGraphManipulator.insertStartNode( graphNode );

        return guid;
    }

    @Override
    public void putCachePath(String path, GUID guid) {
        this.mVectorGraphPathCacheManipulator.insert( path, guid );
    }

    @Override
    public GraphNode get(GUID guid) {
        return this.mVectorGraphManipulator.queryNode( guid );
    }

    @Override
    public GUID getGuidByCachePath(String path) {
        return this.mVectorGraphPathCacheManipulator.queryGUIDByPath( path );
    }

    @Override
    public List<String> getCachePath(GUID guid) {
        return this.mVectorGraphPathCacheManipulator.getPath( guid );
    }

    @Override
    public void remove(GUID guid) {
        this.mVectorGraphManipulator.removeNode( guid );
        this.mVectorGraphPathCacheManipulator.remove( guid );
    }

    @Override
    public void removeCache(GUID guid) {
        this.mVectorGraphPathCacheManipulator.remove( guid );
    }

    @Override
    public void removeCache(String path) {
        this.mVectorGraphPathCacheManipulator.removeByPath( path );
    }

    @Override
    public List<GraphNode> getChildren(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodes( guid );
    }

    @Override
    public List<GUID> fetchChildrenIds(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodeIds( guid );
    }

    @Override
    public boolean hasOwnProperty(Object elm) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }
}
