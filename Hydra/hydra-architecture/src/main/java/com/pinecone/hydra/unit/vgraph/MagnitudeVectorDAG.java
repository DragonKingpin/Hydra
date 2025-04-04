package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;

import java.util.List;

public class MagnitudeVectorDAG implements MegaVectorDAG{

    protected VectorGraphManipulator                mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator       mVectorGraphPathCacheManipulator;

    protected VectorGraphConfig                     mVectorGraphConfig;


    public MagnitudeVectorDAG( VectorGraphMasterManipulator vectorGraphMasterManipulator ){
        this.mVectorGraphManipulator = vectorGraphMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator = vectorGraphMasterManipulator.getVectorGraphPathCacheManipulator();
    }

    public MagnitudeVectorDAG( VectorGraphMasterManipulator vectorGraphMasterManipulator, VectorGraphConfig vectorGraphConfig ) {
        this(vectorGraphMasterManipulator);
        this.mVectorGraphConfig = vectorGraphConfig;
    }


    @Override
    public boolean hasOwnProperty(Object elm) {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public void insertInletNode(GraphNode graphNode) {
        this.mVectorGraphManipulator.insertStartNode( graphNode );
    }

    @Override
    public void insertIntermediateNode(GUID parentGuid, GraphNode graphNode) {
        this.mVectorGraphManipulator.insertIntermediateNode( parentGuid, graphNode );
    }

    @Override
    public void purge(GUID guid) {
        this.mVectorGraphManipulator.removeNode( guid );
        this.removeCachePath( guid );
    }

    @Override
    public GraphNode getGraphNode(GUID guid) {
        return this.mVectorGraphManipulator.queryNode(guid);
    }

    @Override
    public GraphNode getGraphNode(String path) {
        GUID guid = this.queryGUIDByPath(path);
        return this.mVectorGraphManipulator.queryNode( guid );
    }

    @Override
    public GUID queryGUIDByPath(String path) {
        return this.mVectorGraphPathCacheManipulator.queryGUIDByPath( path );
    }

    @Override
    public GraphNode updateGraphNode(GraphNode graphNode) {
        return null;
    }

    @Override
    public List<GraphNode> fetchChildren(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodes( guid );
    }

    @Override
    public List<GUID> fetchChildrenGuids(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodeGuids( guid );
    }

    @Override
    public String getCachePath(GUID guid) {
        return this.mVectorGraphPathCacheManipulator.getPath( guid );
    }

    @Override
    public void removeCachePath(GUID guid) {
        this.mVectorGraphPathCacheManipulator.remove( guid );
    }
}
