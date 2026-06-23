package com.walnut.odin.formation.service;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.FormationInstrument;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.entity.GenericRun;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.dto.FormationRunSubmitRequest;
import com.walnut.odin.formation.dto.FormationRunSubmitResult;
import com.walnut.odin.formation.dto.FormationRuntimeSnapshot;
import com.walnut.odin.formation.schedule.FormationRunPreparator;
import com.walnut.odin.formation.schedule.FormationScheduler;
import com.walnut.odin.formation.source.RunManipulator;

public class RavenFormationService implements FormationService {

    protected FormationRunPreparator mRunPreparator;
    protected FormationInstrument    mFormationInstrument;
    protected RunManipulator         mRunManipulator;
    protected FormationScheduler     mScheduler;
    protected FormationDispatcher    mDispatcher;

    public RavenFormationService(
            FormationRunPreparator runPreparator,
            FormationInstrument formationInstrument,
            FormationScheduler scheduler,
            FormationDispatcher dispatcher
    ) {
        this.mRunPreparator = runPreparator;
        this.mFormationInstrument = formationInstrument;
        this.mRunManipulator = formationInstrument.masterManipulator().runManipulator();
        this.mScheduler = scheduler;
        this.mDispatcher = dispatcher;
    }

    @Override
    public FormationRunSubmitResult submitRun( FormationRunSubmitRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "FormationRunSubmitRequest is null." );
        }

        GenericRun run = (GenericRun)this.mRunPreparator.prepareRun( request );
        FormationRunSubmitResult result = new FormationRunSubmitResult();
        result.setRunGuid( run.getGuid() );
        result.setFormationGuid( run.getFormationGuid() );
        result.setRunStatus( run.getRunStatus() );
        result.setTotalCount( run.getTotalCount() );
        if ( request.isAutoDispatch() && this.mScheduler != null ) {
            this.mScheduler.pulse();
            result.setDispatched( true );
        }
        return result;
    }

    @Override
    public boolean cancelRun( GUID runGuid ) {
        if ( runGuid == null ) {
            return false;
        }
        return this.mRunManipulator.updateStatus( runGuid, FormationRunStatus.Cancelled.getName() ) > 0;
    }

    @Override
    public Object retrieveRun( GUID runGuid ) {
        if ( runGuid == null ) {
            return null;
        }
        return this.mRunManipulator.selectByGuid( runGuid );
    }

    @Override
    public FormationRuntimeSnapshot retrieveRuntimeSnapshot() {
        FormationRuntimeSnapshot snapshot = new FormationRuntimeSnapshot();
        if ( this.mScheduler != null ) {
            snapshot.setSchedulerSnapshot( this.mScheduler.retrieveRuntimeSnapshot() );
        }
        if ( this.mDispatcher != null ) {
            snapshot.setDispatcherSnapshot( this.mDispatcher.retrieveRuntimeSnapshot() );
        }
        return snapshot;
    }
}
