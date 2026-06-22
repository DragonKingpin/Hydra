package com.walnut.odin.formation.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.FormationStrategyType;
import com.walnut.odin.formation.GenericFormationRun;
import com.walnut.odin.formation.GenericFormationStrategyContext;
import com.walnut.odin.formation.GenericFormationStrategyRuntime;
import com.walnut.odin.formation.flow.GenericFormationFlow;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;
import com.walnut.odin.formation.strategy.fixed.FixedPageFormationStrategy64;

public class FormationFlowService {
    protected GuidAllocator           mGuidAllocator;
    protected FormationRunMapper      mRunMapper;
    protected FormationRunPageMapper  mPageMapper;
    protected FormationRunFrameMapper mFrameMapper;

    public FormationFlowService(
            GuidAllocator guidAllocator,
            FormationRunMapper runMapper,
            FormationRunPageMapper pageMapper,
            FormationRunFrameMapper frameMapper
    ) {
        this.mGuidAllocator = guidAllocator;
        this.mRunMapper = runMapper;
        this.mPageMapper = pageMapper;
        this.mFrameMapper = frameMapper;
    }

    public GenericFormationFlow fixedPageFlow( GenericFormationRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        GenericFormationStrategyRuntime runtime = this.runtimeOf( run );
        FixedPageFormationStrategy64 strategy = new FixedPageFormationStrategy64(
                run.getGuid(),
                this.mGuidAllocator,
                this.mRunMapper,
                this.mPageMapper,
                this.mFrameMapper,
                claimOwner
        );
        GenericFormationFlow flow = new GenericFormationFlow( strategy, runtime );
        flow.prepare( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
        return flow;
    }

    public void flow( GenericFormationRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        if ( !FormationStrategyType.FixedPage.name().equals( run.getStrategyType() ) ) {
            throw new UnsupportedOperationException( "Only FixedPage formation strategy is implemented in minimal kernel." );
        }
        this.fixedPageFlow( run, taskScheduler, claimOwner )
                .flow( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
    }

    protected GenericFormationStrategyRuntime runtimeOf( GenericFormationRun run ) {
        GenericFormationStrategyRuntime runtime = new GenericFormationStrategyRuntime();
        runtime.setPageSize( run.getPageSize() );
        runtime.setFrameSize( run.getFrameSize() );
        runtime.setWindowSize( run.getWindowSize() );
        runtime.setInflightLimit( run.getInflightLimit() );
        runtime.setProductsSum( ( run.getTotalCount() + run.getPageSize() - 1L ) / Math.max( 1L, run.getPageSize() ) );
        return runtime;
    }
}
