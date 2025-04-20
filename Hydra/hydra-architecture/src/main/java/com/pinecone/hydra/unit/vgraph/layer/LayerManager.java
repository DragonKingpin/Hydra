package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface LayerManager extends KOMInstrument {
    LayerConfig getConfig();

    Hydrarum getHydrarum();
}
