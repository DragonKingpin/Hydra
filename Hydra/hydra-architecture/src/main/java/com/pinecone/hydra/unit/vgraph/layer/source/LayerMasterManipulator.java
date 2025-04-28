package com.pinecone.hydra.unit.vgraph.layer.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface LayerMasterManipulator extends KOIMasterManipulator {
    LayerManipulator            getLayerManipulator();

    NamespaceManipulator        getNamespaceManipulator();
}
