package com.walnut.odin.formation.strategy.preemptive;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.flow.ArchFormationProducer;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.GenericFormationFrame;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.FormationStrategyContext;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;
import com.walnut.odin.formation.strategy.GenericFormationStrategyRuntime;

public class PreemptiveFrameFormationProducer64 extends ArchFormationProducer<FormationFrame>
        implements PreemptiveFrameFormationProducer {
    protected FormationStrategyContext mContext;
    protected FormationStrategyRuntime mRuntime;
    protected GUID                     mRunGuid;
    protected GuidAllocator            mGuidAllocator;
    protected MasterManipulator        mMasterManipulator;
    protected long                     mnLeaseSeconds = 300L;

    public PreemptiveFrameFormationProducer64(
            FormationStrategyContext context,
            FormationStrategyRuntime runtime,
            GUID runGuid,
            GuidAllocator guidAllocator,
            MasterManipulator masterManipulator,
            String claimOwner,
            long productsSum
    ) {
        super( claimOwner, productsSum );
        this.mContext = context;
        this.mRuntime = runtime;
        this.mRunGuid = runGuid;
        this.mGuidAllocator = guidAllocator;
        this.mMasterManipulator = masterManipulator;
    }

    @Override
    public FormationFrame require() {
        long claimLimit = Math.max( 1L, this.mRuntime.inflightLimit() );
        List<? extends FormationFrame> frames = this.mMasterManipulator.frameManipulator().fetchPendingFrames( this.mRunGuid, claimLimit );
        if ( frames == null || frames.isEmpty() ) {
            return null;
        }

        for ( FormationFrame frame : frames ) {
            GUID frameGuid = this.resolveFrameGuid( frame );
            if ( frameGuid == null ) {
                continue;
            }
            GUID claimToken = this.mGuidAllocator.nextGUID();
            if ( this.mMasterManipulator.frameManipulator().claimFrame( frameGuid, this.mszClaimOwner, claimToken, this.mnLeaseSeconds ) <= 0 ) {
                continue;
            }
            if ( this.mRuntime instanceof GenericFormationStrategyRuntime ) {
                ( (GenericFormationStrategyRuntime)this.mRuntime ).increaseProducedCount();
            }
            return frame;
        }

        return null;
    }

    @Override
    public void deactivate( FormationFrame product ) {
        this.countDown();
    }

    @Override
    public boolean hasMoreProducts() {
        return !this.isFinished();
    }

    protected GUID resolveFrameGuid( FormationFrame frame ) {
        return frame instanceof GenericFormationFrame
                ? ( (GenericFormationFrame)frame ).getGuid()
                : null;
    }
}
