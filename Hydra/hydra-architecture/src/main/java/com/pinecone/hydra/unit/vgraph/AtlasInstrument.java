package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.Layer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;

import java.util.List;

public interface AtlasInstrument extends Instrument {
    AtlasInstrument parent();

    LayerInstrument layerInstrument();


    Processum getSuperiorProcess();

    AtlasMasterManipulator getMasterManipulator();

    VectorGraphConfig getConfig();

    void setParent( AtlasInstrument atlasInstrument );

    GuidAllocator getGuidAllocator();

    GUID queryParentID( GUID guid );

    boolean contains( GUID handleNode, GUID nodeGuid );

    GUID put( GraphNode graphNode );

    GUID put( GUID parentGuid, GraphNode graphNode );

    GraphNode get( GUID guid );

    TreeNode get(GUID guid, int depth );

    void remove( GUID guid );

    List<GraphNode> getChildren( GUID guid );

    List<GUID > fetchChildrenIds(GUID guid );

    void rename( GUID guid, String name );

    VectorDAG toVectorDAG( Layer layer );

    void addChild( GUID parentGuid, GUID childGuid );

}
