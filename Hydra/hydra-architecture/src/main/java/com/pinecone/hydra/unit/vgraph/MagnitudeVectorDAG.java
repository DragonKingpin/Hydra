package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphPathCacheManipulator;
import com.pinecone.ulf.util.guid.GenericGuidAllocator;

import java.util.List;

public class MagnitudeVectorDAG extends ArchVectorDAG implements MegaVectorDAG {
    public MagnitudeVectorDAG( List<GUID> handleNodeGuids,VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig) {
        super(handleNodeGuids,masterManipulator, vectorGraphConfig);
    }

    @Override
    public VectorGraphMasterManipulator getMasterManipulator() {
        return this.mMasterManipulator;
    }

    @Override
    public GUID putHandleNode(GraphNode graphNode) {
        GUID guid = this.mGuidAllocator.nextGUID();
        graphNode.setId( guid );
        this.mVectorGraphManipulator.insertHandleNode( graphNode );

        return guid;
    }

    @Override
    public void putNodeByEdge(GUID parentGuid, GraphNode graphNode) {
        this.mVectorGraphManipulator.insertNodeByEdge(parentGuid, graphNode);
    }

    @Override
    public void putCachePath(String path, GUID guid) {
        this.mVectorGraphPathCacheManipulator.insert( path, guid );
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
    public List<GUID> fetchParentIds(GUID guid) {
        return this.mVectorGraphManipulator.fetchParentIds(guid);
    }

    @Override
    public VectorDAG queryVectorDAG(GUID guid) {
        return null;
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
