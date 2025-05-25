package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface LayerInstrument extends KOMInstrument {
    LayerConfig LayerConfig = new VLayerConfig();

    LayerConfig getConfig();

    Hydrogen getHydrogen();

    void addChild( GUID parentGuid, GUID childGuid );

    void update( TreeNode treeNode );

}
