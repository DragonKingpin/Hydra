package com.walnut.odin.formation.schedule;

import java.time.LocalDateTime;

import com.pinecone.framework.system.regime.Orchestrator;
import com.walnut.odin.formation.dto.FormationSchedulerRuntimeSnapshot;

public interface FormationScheduler extends Orchestrator {

    void startup();

    void shutdown();

    void pulse();

    void pulse( LocalDateTime pulseTime );

    FormationSchedulerRuntimeSnapshot retrieveRuntimeSnapshot();
}
