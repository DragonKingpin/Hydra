package com.walnut.odin.formation.service;

import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.strategy.FormationStrategyType;
import com.walnut.odin.formation.entity.GenericRun;
import com.walnut.odin.formation.strategy.GenericFormationStrategyContext;
import com.walnut.odin.formation.strategy.GenericFormationStrategyRuntime;
import com.walnut.odin.formation.flow.GenericFormationFlow;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.fixed.FixedPageFormationStrategy64;

public class FormationFlowService implements FlowService {
    protected GuidAllocator           mGuidAllocator;
    protected MasterManipulator       mMasterManipulator;

    public FormationFlowService(
            GuidAllocator guidAllocator,
            MasterManipulator masterManipulator
    ) {
        this.mGuidAllocator = guidAllocator;
        this.mMasterManipulator = masterManipulator;
    }

    public GenericFormationFlow fixedPageFlow( GenericRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        GenericFormationStrategyRuntime runtime = this.runtimeOf( run );
        FixedPageFormationStrategy64 strategy = new FixedPageFormationStrategy64(
                run.getGuid(),
                this.mGuidAllocator,
                this.mMasterManipulator,
                claimOwner
        );
        GenericFormationFlow flow = new GenericFormationFlow( strategy, runtime );
        flow.prepare( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
        return flow;
    }

    public void flow( GenericRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        if ( !FormationStrategyType.FixedPage.name().equals( run.getStrategyType() ) ) {
            throw new UnsupportedOperationException( "Only FixedPage formation strategy is implemented in minimal kernel." );
        }
        this.fixedPageFlow( run, taskScheduler, claimOwner )
                .flow( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
    }

    @Override
    public void flow( com.walnut.odin.formation.entity.RunEntry run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        this.flow( (GenericRun)run, taskScheduler, claimOwner );
    }

    protected GenericFormationStrategyRuntime runtimeOf( GenericRun run ) {
        GenericFormationStrategyRuntime runtime = new GenericFormationStrategyRuntime();
        runtime.setPageSize( run.getPageSize() );
        runtime.setFrameSize( run.getFrameSize() );
        runtime.setWindowSize( run.getWindowSize() );
        runtime.setInflightLimit( run.getInflightLimit() );
        runtime.setProductsSum( ( run.getTotalCount() + run.getPageSize() - 1L ) / Math.max( 1L, run.getPageSize() ) );
        return runtime;
    }
}
