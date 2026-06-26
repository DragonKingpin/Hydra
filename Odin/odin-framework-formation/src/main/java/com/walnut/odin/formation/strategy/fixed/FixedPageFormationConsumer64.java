package com.walnut.odin.formation.strategy.fixed;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.chunk.Page;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;
import com.walnut.odin.formation.flow.ArchFormationConsumer;
import com.walnut.odin.formation.flow.FormationFrameConsumerAdapter;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationFrameFeedback;
import com.walnut.odin.formation.plan.FormationPage;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.GenericFormationStrategyRuntime;
import com.walnut.odin.task.RavenTaskInstance;

public class FixedPageFormationConsumer64 extends ArchFormationConsumer<FormationPage>
        implements FixedPageFormationConsumer {
    protected FormationStrategyRuntime mRuntime;
    protected MasterManipulator        mMasterManipulator;
    protected GUID                     mRunGuid;

    public FixedPageFormationConsumer64(
            FixedPageFormationProducer producer,
            FormationFrameConsumerAdapter frameConsumerAdapter,
            FormationStrategyRuntime runtime,
            GUID runGuid,
            MasterManipulator masterManipulator
    ) {
        super( producer, frameConsumerAdapter );
        this.mRuntime = runtime;
        this.mRunGuid = runGuid;
        this.mMasterManipulator = masterManipulator;
    }

    @Override
    public FixedPageFormationProducer producer() {
        return (FixedPageFormationProducer)this.mProducer;
    }

    @Override
    public void consume() {
        while ( this.mProducer.hasMoreProducts() ) {
            if ( this.mProducer.hasTerminateSignal() || Thread.currentThread().isInterrupted() ) {
                break;
            }

            FormationPage page = this.mProducer.require();
            if ( page == null ) {
                break;
            }

            try {
                for ( FormationFrame frame : page.frames() ) {
                    this.consumeFrame( frame );
                }
            }
            finally {
                this.mProducer.deactivate( page );
            }
        }
    }

    @Override
    public void consume( Page unit ) {
        if ( unit instanceof FormationPage ) {
            FormationPage page = (FormationPage)unit;
            for ( FormationFrame frame : page.frames() ) {
                this.consumeFrame( frame );
            }
        }
    }

    protected void consumeFrame( FormationFrame frame ) {
        try {
            FormationFrameFeedback feedback = this.mFrameConsumerAdapter.consumeFrame( frame );
            GUID instanceGuid = this.requireInstanceGuid( feedback.submitResult() );
            int nUpdated = this.mMasterManipulator.frameManipulator().markSubmitted( frame instanceof com.walnut.odin.formation.plan.GenericFormationFrame
                    ? ( (com.walnut.odin.formation.plan.GenericFormationFrame)frame ).getGuid()
                    : null, instanceGuid );
            if ( nUpdated > 0 ) {
                this.mMasterManipulator.runManipulator().increaseSubmitted( this.mRunGuid );
            }
            if ( this.mRuntime instanceof GenericFormationStrategyRuntime) {
                ( (GenericFormationStrategyRuntime)this.mRuntime ).increaseConsumedCount();
            }
        }
        catch ( Exception e ) {
            int nUpdated = this.mMasterManipulator.frameManipulator().markFailed(
                    frame instanceof com.walnut.odin.formation.plan.GenericFormationFrame
                            ? ( (com.walnut.odin.formation.plan.GenericFormationFrame)frame ).getGuid()
                            : null,
                    e.getMessage()
            );
            if ( nUpdated > 0 ) {
                this.mMasterManipulator.runManipulator().increaseFailed( this.mRunGuid );
            }
        }
    }

    protected GUID resolveInstanceGuid( TaskInstantaneousSubmitResult result ) {
        if ( result == null ) {
            return null;
        }
        RavenTaskInstance instance = result.getInstance();
        if ( instance == null || instance.getInstanceEntry() == null ) {
            return null;
        }
        return instance.getInstanceEntry().getGuid();
    }

    protected GUID requireInstanceGuid( TaskInstantaneousSubmitResult result ) {
        GUID instanceGuid = this.resolveInstanceGuid( result );
        if ( instanceGuid == null ) {
            throw new IllegalStateException( "Formation frame submit result has no task instance guid." );
        }
        return instanceGuid;
    }
}
