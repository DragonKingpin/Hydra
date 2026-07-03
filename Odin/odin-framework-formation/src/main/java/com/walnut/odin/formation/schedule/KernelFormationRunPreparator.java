package com.walnut.odin.formation.schedule;

import com.walnut.odin.formation.dto.FormationRunSubmitRequest;
import com.walnut.odin.formation.entity.RunEntry;
import com.walnut.odin.formation.service.RunService;

public class KernelFormationRunPreparator implements FormationRunPreparator {

    protected RunService mRunService;

    public KernelFormationRunPreparator( RunService runService ) {
        this.mRunService = runService;
    }

    @Override
    public RunEntry prepareRun( FormationRunSubmitRequest request ) {
        return this.mRunService.createRun( request );
    }
}
