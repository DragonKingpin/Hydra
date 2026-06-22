package com.walnut.odin.formation.schedule;

import com.walnut.odin.formation.GenericFormationRun;
import com.walnut.odin.formation.entity.FormationRunSubmitRequest;
import com.walnut.odin.formation.service.FormationRunService;

public class KernelFormationRunPreparator implements FormationRunPreparator {

    protected FormationRunService mRunService;

    public KernelFormationRunPreparator( FormationRunService runService ) {
        this.mRunService = runService;
    }

    @Override
    public GenericFormationRun prepareRun( FormationRunSubmitRequest request ) {
        return this.mRunService.createRun( request );
    }
}
