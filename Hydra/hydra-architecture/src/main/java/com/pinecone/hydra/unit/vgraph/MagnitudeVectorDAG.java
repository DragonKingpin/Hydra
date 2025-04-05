package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;

import java.util.List;

public class MagnitudeVectorDAG extends ArchAtlasInstrument implements MegaVectorDAG{

    protected VectorGraphManipulator                mVectorGraphManipulator;

    protected VectorGraphPathCacheManipulator       mVectorGraphPathCacheManipulator;

    protected VectorGraphConfig                     mVectorGraphConfig;


    public MagnitudeVectorDAG( Processum superiorProcess, VectorGraphMasterManipulator vectorGraphMasterManipulator, VectorGraphConfig vectorGraphConfig, AtlasInstrument parent, String name ){
        super(superiorProcess, vectorGraphMasterManipulator, vectorGraphConfig, parent, name );
        this.mVectorGraphManipulator = vectorGraphMasterManipulator.getVectorGraphManipulator();
        this.mVectorGraphPathCacheManipulator = vectorGraphMasterManipulator.getVectorGraphPathCacheManipulator();
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
    public void insertNode(GUID parentGuid, GraphNode graphNode) {
        this.mVectorGraphManipulator.insertNode( parentGuid, graphNode );
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
        GUID guid = this.queryIdByPath(path);
        return this.mVectorGraphManipulator.queryNode( guid );
    }

    @Override
    public GUID queryIdByPath(String path) {
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
    public List<GUID> fetchChildrenIds(GUID guid) {
        return this.mVectorGraphManipulator.fetchChildNodeIds( guid );
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
