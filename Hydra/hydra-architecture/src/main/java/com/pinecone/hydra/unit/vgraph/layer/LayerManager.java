package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface LayerManager extends KOMInstrument {
    LayerConfig LayerConfig = new VLayerConfig();
    LayerConfig getConfig();

    Hydrarum getHydrarum();

    void addChild(GUID parentGuid, GUID childGuid);
}
