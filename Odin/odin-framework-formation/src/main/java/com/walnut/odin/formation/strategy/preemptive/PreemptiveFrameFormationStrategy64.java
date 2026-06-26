package com.walnut.odin.formation.strategy.preemptive;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.slime.chunk.flow.ChunkFlowRuntime;
import com.walnut.odin.formation.flow.FormationFrameConsumerAdapter;
import com.walnut.odin.formation.flow.UniformFormationFrameConsumerAdapter;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.FormationStrategyContext;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;
import com.walnut.odin.formation.strategy.FormationStrategyType;

public class PreemptiveFrameFormationStrategy64 implements PreemptiveFrameFormationStrategy {
    protected GUID                            mRunGuid;
    protected GuidAllocator                   mGuidAllocator;
    protected MasterManipulator               mMasterManipulator;
    protected String                          mszClaimOwner;
    protected PreemptiveFrameFormationProducer mProducer;

    public PreemptiveFrameFormationStrategy64(
            GUID runGuid,
            GuidAllocator guidAllocator,
            MasterManipulator masterManipulator,
            String claimOwner
    ) {
        this.mRunGuid = runGuid;
        this.mGuidAllocator = guidAllocator;
        this.mMasterManipulator = masterManipulator;
        this.mszClaimOwner = claimOwner;
    }

    @Override
    public FormationStrategyType kind() {
        return FormationStrategyType.PreemptiveFrame;
    }

    @Override
    public void prepare( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        this.mMasterManipulator.runManipulator().markRunning( this.mRunGuid );
    }

    @Override
    public PreemptiveFrameFormationProducer formulateProducer( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        if ( this.mProducer != null ) {
            return this.mProducer;
        }
        this.mProducer = new PreemptiveFrameFormationProducer64(
                context,
                runtime,
                this.mRunGuid,
                this.mGuidAllocator,
                this.mMasterManipulator,
                this.mszClaimOwner,
                runtime.productsSum()
        );
        return this.mProducer;
    }

    @Override
    public PreemptiveFrameFormationConsumer formulateConsumer( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        FormationFrameConsumerAdapter adapter = new UniformFormationFrameConsumerAdapter( context.taskScheduler() );
        return new PreemptiveFrameFormationConsumer64(
                this.formulateProducer( context, runtime ),
                adapter,
                runtime,
                this.mRunGuid,
                this.mMasterManipulator
        );
    }

    @Override
    public boolean hasTerminateSignal( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        return false;
    }

    @Override
    public boolean isFinished( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        return runtime != null && runtime.isFinished();
    }

    @Override
    public void prepare( ChunkFlowRuntime runtime ) {
    }

    @Override
    public boolean hasNext( ChunkFlowRuntime runtime ) {
        return false;
    }
}
