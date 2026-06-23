package com.walnut.odin.system;

import com.pinecone.framework.system.SynergicSystem;
import com.pinecone.hydra.system.centrum.CentralControlSubsystem;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.FormationColonel;

public interface TaskCentralControl extends SynergicSystem, CentralControlSubsystem {

    RuntimeAtlasInstrument atlasInstrument();

    CollectiveTaskRegiment taskRegiment();

    UniformTaskScheduler taskScheduler();

    FormationColonel formationColonel();
}
