package com.walnut.odin.system;

import com.pinecone.framework.system.SynergicSystem;
import com.pinecone.hydra.system.centrum.CentralControlSubsystem;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;

public interface TaskCentralControl extends SynergicSystem, CentralControlSubsystem {

    LayerInstrument layerInstrument();

    RuntimeAtlasInstrument atlasInstrument();

    CollectiveTaskRegiment taskRegiment();

    UniformTaskScheduler taskScheduler();
}
