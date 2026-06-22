package com.walnut.odin.formation;

import com.pinecone.framework.system.regime.arch.Manager;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.schedule.FormationScheduler;
import com.walnut.odin.formation.service.FormationService;

public interface FormationManager extends Manager {

    void prepareFormation();

    void startupFormation();

    void shutdownFormation();

    FormationService formationService();

    FormationScheduler formationScheduler();

    FormationDispatcher formationDispatcher();
}
