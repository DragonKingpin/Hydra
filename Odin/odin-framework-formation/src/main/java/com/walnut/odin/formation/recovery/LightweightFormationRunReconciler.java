package com.walnut.odin.formation.recovery;

import java.time.LocalDateTime;
import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.FormationConfig;
import com.walnut.odin.formation.FormationFrameStatus;
import com.walnut.odin.formation.FormationInstrument;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.entity.RunEntry;
import com.walnut.odin.formation.lifecycle.FormationRunTerminalResolver;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.source.FrameManipulator;
import com.walnut.odin.formation.source.RunManipulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightweightFormationRunReconciler implements FormationRunReconciler {

    private static final Logger log = LoggerFactory.getLogger( LightweightFormationRunReconciler.class );

    protected FormationConfig      mConfig;
    protected FormationInstrument  mFormationInstrument;
    protected UniformTaskScheduler mTaskScheduler;
    protected RunManipulator       mRunManipulator;
    protected FrameManipulator     mFrameManipulator;
    protected FormationRunTerminalResolver mTerminalResolver;

    public LightweightFormationRunReconciler(
            FormationConfig config,
            FormationInstrument formationInstrument,
            UniformTaskScheduler taskScheduler
    ) {
        this.mConfig = config;
        this.mFormationInstrument = formationInstrument;
        this.mTaskScheduler = taskScheduler;
        this.mRunManipulator = formationInstrument.masterManipulator().runManipulator();
        this.mFrameManipulator = formationInstrument.masterManipulator().frameManipulator();
        this.mTerminalResolver = new FormationRunTerminalResolver( this.mRunManipulator );
    }

    @Override
    public void reconcileStartup() {
        if ( this.mConfig.isFormationRecoveryEnabled() && this.mConfig.isFormationStartupReconcileEnabled() ) {
            this.reconcileRuns( LocalDateTime.now() );
            log.info( "[OdinFormation] [StartupReconcile] (Mode: `lightweight`) <Done>" );
        }
    }

    @Override
    public void reconcilePulse( LocalDateTime pulseTime ) {
        if ( this.mConfig.isFormationRecoveryEnabled() ) {
            this.reconcileRuns( pulseTime );
            log.debug( "[OdinFormation] [PulseReconcile] (PulseTime: `{}`) <Done>", pulseTime );
        }
    }

    protected void reconcileRuns( LocalDateTime pulseTime ) {
        long nLimit = Math.max( 1L, this.mConfig.getFormationDispatcherPollBatchSize() );
        List<RunEntry> runs = this.mRunManipulator.pageRuns(
                null,
                null,
                FormationRunStatus.Running.getName(),
                0L,
                nLimit
        );
        if ( runs == null || runs.isEmpty() ) {
            return;
        }

        for ( RunEntry run : runs ) {
            if ( run == null || run.getGuid() == null ) {
                continue;
            }
            this.reconcileRun( run.getGuid(), pulseTime );
        }
    }

    protected void reconcileRun( GUID runGuid, LocalDateTime pulseTime ) {
        this.reconcileSubmittedFrames( runGuid );
        this.completeRunIfTerminal( runGuid );
    }

    protected void reconcileSubmittedFrames( GUID runGuid ) {
        long nLimit = Math.max( 1L, this.mConfig.getFormationDispatcherPollBatchSize() );
        List<FormationFrame> frames = this.mFrameManipulator.pageFrames(
                runGuid,
                FormationFrameStatus.Submitted.getName(),
                0L,
                nLimit
        );
        if ( frames == null || frames.isEmpty() ) {
            return;
        }

        for ( FormationFrame frame : frames ) {
            this.reconcileSubmittedFrame( frame );
        }
    }

    protected void reconcileSubmittedFrame( FormationFrame frame ) {
        if ( frame == null || frame.getGuid() == null || frame.getInstanceGuid() == null ) {
            return;
        }

        InstanceEntry instanceEntry = this.mTaskScheduler.instanceInstrument().getInstanceEntry( frame.getInstanceGuid() );
        if ( instanceEntry == null || instanceEntry.getInstanceStatus() == null ) {
            return;
        }

        TaskInstanceStatus instanceStatus = instanceEntry.getInstanceStatus();
        if ( instanceStatus == TaskInstanceStatus.Finished ) {
            if ( this.mFrameManipulator.markCompleted( frame.getGuid() ) > 0 ) {
                this.mRunManipulator.increaseCompleted( frame.getRunGuid() );
            }
            return;
        }

        if ( this.isFailedInstanceStatus( instanceStatus ) ) {
            if ( this.mFrameManipulator.markFailed( frame.getGuid(), this.resolveErrorCause( instanceEntry ) ) > 0 ) {
                this.mRunManipulator.increaseFailed( frame.getRunGuid() );
            }
        }
    }

    protected void completeRunIfTerminal( GUID runGuid ) {
        if ( this.hasUnfinishedFrames( runGuid ) ) {
            return;
        }

        this.mTerminalResolver.resolve( runGuid );
    }

    protected boolean hasUnfinishedFrames( GUID runGuid ) {
        return this.mFrameManipulator.countFrames( runGuid, FormationFrameStatus.Pending.getName() ) > 0L
                || this.mFrameManipulator.countFrames( runGuid, FormationFrameStatus.Claimed.getName() ) > 0L
                || this.mFrameManipulator.countFrames( runGuid, FormationFrameStatus.Submitted.getName() ) > 0L
                || this.mFrameManipulator.countFrames( runGuid, FormationFrameStatus.DependencyWait.getName() ) > 0L;
    }

    protected boolean isFailedInstanceStatus( TaskInstanceStatus status ) {
        return status == TaskInstanceStatus.Error
                || status == TaskInstanceStatus.AuditFailed
                || status == TaskInstanceStatus.Killed;
    }

    protected String resolveErrorCause( InstanceEntry instanceEntry ) {
        if ( instanceEntry.getErrorCause() != null && !instanceEntry.getErrorCause().isEmpty() ) {
            return instanceEntry.getErrorCause();
        }
        return "Task instance terminated with status: " + instanceEntry.getInstanceStatus().getName();
    }
}
