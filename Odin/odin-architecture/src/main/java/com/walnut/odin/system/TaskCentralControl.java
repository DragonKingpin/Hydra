package com.walnut.odin.system;

import com.pinecone.hydra.system.imperium.FacilityClass;
import com.pinecone.hydra.system.imperium.FacilitySynergicSystem;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.FormationColonel;

public interface TaskCentralControl extends FacilitySynergicSystem {

    RuntimeAtlasInstrument atlasInstrument();

    CollectiveTaskRegiment taskRegiment();

    UniformTaskScheduler taskScheduler();

    FormationColonel formationColonel();

    @Override
    default FacilityClass facilityClass() {
        return FacilityClass.Task;
    }

    @Override
    default FacilityClass[] ownedClass() {
        return new FacilityClass[]{ FacilityClass.Task, FacilityClass.Process };
    }
}
