package com.pinecone.hydra.unit.vgraph;

import java.util.List;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphManipulator;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;
import com.pinecone.ulf.util.guid.GUIDs;

public abstract class ArchAtlasInstrument implements AtlasInstrument {

    protected AtlasInstrument              mParentInstrument;
    protected LayerInstrument              mLayerInstrument;

    protected Hydrogen                     mHydrogen;
    protected Processum                    mSuperiorProcess;

    protected GuidAllocator                mGuidAllocator;

    protected AtlasMasterManipulator       mAtlasMasterManipulator;
    protected VectorGraphMasterManipulator mVectorGraphMasterManipulator;
    protected VectorGraphManipulator       mVectorGraphManipulator;

    protected VectorGraphConfig            mVectorGraphConfig;

    public ArchAtlasInstrument(
            AtlasMappingDriver atlasMappingDriver,
            VectorGraphConfig vectorGraphConfig,
            LayerInstrument layerInstrument
    ) {
        this.mLayerInstrument              = layerInstrument;
        this.mVectorGraphConfig            = vectorGraphConfig;
        this.mSuperiorProcess              = atlasMappingDriver.getSuperiorProcess();
        this.mAtlasMasterManipulator       = atlasMappingDriver.getMasterManipulator();
        this.mVectorGraphMasterManipulator = this.mAtlasMasterManipulator.getVectorGraphMasterManipulator();
        this.mVectorGraphManipulator       = this.mVectorGraphMasterManipulator.getVectorGraphManipulator();

        if ( this.mSuperiorProcess instanceof Hydrogen ) {
            this.mHydrogen = (Hydrogen) this.mSuperiorProcess;
        }
        else {
            this.mHydrogen = (Hydrogen) this.mSuperiorProcess.parentSystem();
        }
        this.mGuidAllocator = GUIDs.newGuidAllocator();
    }

    public ArchAtlasInstrument( AtlasMappingDriver driver, LayerInstrument layerInstrument ) {
        this( driver, null, layerInstrument );
    }

    @Override
    public LayerInstrument layerInstrument() {
        return this.mLayerInstrument;
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
    public void setParent( AtlasInstrument atlasInstrument ) {
        this.mParentInstrument = atlasInstrument;
    }

    @Override
    public AtlasMasterManipulator getMasterManipulator() {
        return this.mAtlasMasterManipulator;
    }

    @Override
    public VectorGraphConfig getConfig() {
        return this.mVectorGraphConfig;
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mGuidAllocator;
    }

    @Override
    public GUID queryParentID( GUID guid ) {
        return null;
    }

    @Override
    public boolean contains( GUID handleNode, GUID nodeGuid ) {
        if ( handleNode == null || nodeGuid == null ) {
            return false;
        }
        if ( handleNode.equals( nodeGuid ) ) {
            return true;
        }

        List<GraphNode> nodes = this.mVectorGraphManipulator.fetchChildNodes( handleNode );
        for ( GraphNode node : nodes ) {
            if ( node != null && this.contains( node.getId(), nodeGuid ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GUID put( GraphNode graphNode ) {
        GUID guid = this.mGuidAllocator.nextGUID();
        graphNode.setId( guid );
        this.mVectorGraphManipulator.insertGraphNode( graphNode );

        return guid;
    }

    @Override
    public GUID put( GUID parentGuid, GraphNode graphNode ) {
        GUID guid = this.mGuidAllocator.nextGUID();
        graphNode.setId( guid );
        this.mVectorGraphManipulator.insertNodeByEdge( parentGuid, graphNode );
        return guid;
    }

    @Override
    public GraphNode get( GUID guid ) {
        return this.mVectorGraphManipulator.queryNode( guid );
    }

    @Override
    public TreeNode get( GUID guid, int depth ) {
        return null;
    }

    @Override
    public void remove( GUID guid ) {
        this.mVectorGraphManipulator.removeNode( guid );
    }

    @Override
    public List<GraphNode> getChildren( GUID guid ) {
        return this.mVectorGraphManipulator.fetchChildNodes( guid );
    }

    @Override
    public List<GUID> fetchChildrenIds( GUID guid ) {
        return this.mVectorGraphManipulator.fetchChildNodeIds( guid );
    }

    @Override
    public void rename( GUID guid, String name ) {

    }
}
