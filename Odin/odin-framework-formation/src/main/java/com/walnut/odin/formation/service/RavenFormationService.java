package com.walnut.odin.formation.service;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.GenericFormationRun;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.entity.FormationRunSubmitRequest;
import com.walnut.odin.formation.entity.FormationRunSubmitResult;
import com.walnut.odin.formation.entity.FormationRuntimeSnapshot;
import com.walnut.odin.formation.schedule.FormationRunPreparator;
import com.walnut.odin.formation.schedule.FormationScheduler;
import com.walnut.odin.formation.source.FormationRunMapper;

public class RavenFormationService implements FormationService {

    protected FormationRunPreparator mRunPreparator;
    protected FormationRunMapper     mRunMapper;
    protected FormationScheduler     mScheduler;
    protected FormationDispatcher    mDispatcher;

    public RavenFormationService(
            FormationRunPreparator runPreparator,
            FormationRunMapper runMapper,
            FormationScheduler scheduler,
            FormationDispatcher dispatcher
    ) {
        this.mRunPreparator = runPreparator;
        this.mRunMapper = runMapper;
        this.mScheduler = scheduler;
        this.mDispatcher = dispatcher;
    }

    @Override
    public FormationRunSubmitResult submitRun( FormationRunSubmitRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "FormationRunSubmitRequest is null." );
        }

        GenericFormationRun run = (GenericFormationRun)this.mRunPreparator.prepareRun( request );
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
        return this.mRunMapper.updateStatus( runGuid, FormationRunStatus.Cancelled.getName() ) > 0;
    }

    @Override
    public Object retrieveRun( GUID runGuid ) {
        if ( runGuid == null ) {
            return null;
        }
        return this.mRunMapper.selectByGuid( runGuid );
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
