package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.List;

public interface LayerInstrument extends KOMInstrument {
    LayerConfig LayerConfig = new VLayerConfig();

    LayerConfig getConfig();

    Hydrogen getHydrogen();

    void addChild( GUID parentGuid, GUID childGuid );

    void update( TreeNode treeNode );

    List<Layer> splitGraphLayer(VectorDAG vectorDAG );

    long countSourceNode( GUID layerGuid );

    List<GUID> fetchSourceGuidsByTaskPriority(GUID layerGuid,long offset, long limit );

}
