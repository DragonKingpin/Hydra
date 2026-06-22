package com.walnut.odin.formation.strategy.fixed;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.slime.chunk.flow.ChunkFlowRuntime;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.FormationStrategyContext;
import com.walnut.odin.formation.FormationStrategyRuntime;
import com.walnut.odin.formation.FormationStrategyType;
import com.walnut.odin.formation.flow.FormationFrameConsumerAdapter;
import com.walnut.odin.formation.flow.UniformFormationFrameConsumerAdapter;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;

public class FixedPageFormationStrategy64 implements FixedPageFormationStrategy {
    protected GUID                    mRunGuid;
    protected GuidAllocator           mGuidAllocator;
    protected FormationRunMapper      mRunMapper;
    protected FormationRunPageMapper  mPageMapper;
    protected FormationRunFrameMapper mFrameMapper;
    protected String                  mszClaimOwner;
    protected FixedPageFormationProducer mProducer;

    public FixedPageFormationStrategy64(
            GUID runGuid,
            GuidAllocator guidAllocator,
            FormationRunMapper runMapper,
            FormationRunPageMapper pageMapper,
            FormationRunFrameMapper frameMapper,
            String claimOwner
    ) {
        this.mRunGuid = runGuid;
        this.mGuidAllocator = guidAllocator;
        this.mRunMapper = runMapper;
        this.mPageMapper = pageMapper;
        this.mFrameMapper = frameMapper;
        this.mszClaimOwner = claimOwner;
    }

    @Override
    public FormationStrategyType kind() {
        return FormationStrategyType.FixedPage;
    }

    @Override
    public void prepare( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        this.mRunMapper.markRunning( this.mRunGuid );
    }

    @Override
    public FixedPageFormationProducer formulateProducer( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        if ( this.mProducer != null ) {
            return this.mProducer;
        }
        this.mProducer = new FixedPageFormationProducer64(
                context,
                runtime,
                this.mRunGuid,
                this.mGuidAllocator,
                this.mPageMapper,
                this.mFrameMapper,
                this.mszClaimOwner,
                runtime.productsSum()
        );
        return this.mProducer;
    }

    @Override
    public FixedPageFormationConsumer formulateConsumer( FormationStrategyContext context, FormationStrategyRuntime runtime ) {
        FormationFrameConsumerAdapter adapter = new UniformFormationFrameConsumerAdapter( context.taskScheduler() );
        return new FixedPageFormationConsumer64(
                this.formulateProducer( context, runtime ),
                adapter,
                runtime,
                this.mRunGuid,
                this.mRunMapper,
                this.mFrameMapper
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
