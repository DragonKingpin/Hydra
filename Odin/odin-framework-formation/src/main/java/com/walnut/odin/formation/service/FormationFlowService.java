package com.walnut.odin.formation.service;

import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.strategy.FormationStrategyType;
import com.walnut.odin.formation.entity.GenericRun;
import com.walnut.odin.formation.strategy.GenericFormationStrategyContext;
import com.walnut.odin.formation.strategy.GenericFormationStrategyRuntime;
import com.walnut.odin.formation.flow.GenericFormationFlow;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.FormationStrategy;
import com.walnut.odin.formation.strategy.fixed.FixedPageFormationStrategy64;
import com.walnut.odin.formation.strategy.preemptive.PreemptiveFrameFormationStrategy64;
import com.walnut.odin.formation.strategy.window.WindowedPageFormationStrategy64;

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
        GenericFormationStrategyRuntime runtime = this.runtimeOf( run, FormationStrategyType.FixedPage );
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

    public GenericFormationFlow windowedPageFlow( GenericRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        GenericFormationStrategyRuntime runtime = this.runtimeOf( run, FormationStrategyType.WindowedPage );
        WindowedPageFormationStrategy64 strategy = new WindowedPageFormationStrategy64(
                run.getGuid(),
                this.mGuidAllocator,
                this.mMasterManipulator,
                claimOwner
        );
        GenericFormationFlow flow = new GenericFormationFlow( strategy, runtime );
        flow.prepare( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
        return flow;
    }

    public GenericFormationFlow preemptiveFrameFlow( GenericRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        GenericFormationStrategyRuntime runtime = this.runtimeOf( run, FormationStrategyType.PreemptiveFrame );
        PreemptiveFrameFormationStrategy64 strategy = new PreemptiveFrameFormationStrategy64(
                run.getGuid(),
                this.mGuidAllocator,
                this.mMasterManipulator,
                claimOwner
        );
        GenericFormationFlow flow = new GenericFormationFlow( strategy, runtime );
        flow.prepare( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
        return flow;
    }

    public GenericFormationFlow flowOf( GenericRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        FormationStrategyType strategyType = this.resolveStrategyType( run );
        FormationStrategy strategy = this.createStrategy( strategyType, run, claimOwner );
        GenericFormationStrategyRuntime runtime = this.runtimeOf( run, strategyType );
        GenericFormationFlow flow = new GenericFormationFlow( strategy, runtime );
        flow.prepare( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
        return flow;
    }

    public void flow( GenericRun run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        this.flowOf( run, taskScheduler, claimOwner )
                .flow( new GenericFormationStrategyContext( run.getFormationGuid(), run.getFormationGuid(), taskScheduler ) );
    }

    @Override
    public void flow( com.walnut.odin.formation.entity.RunEntry run, UniformTaskScheduler taskScheduler, String claimOwner ) {
        this.flow( (GenericRun)run, taskScheduler, claimOwner );
    }

    protected FormationStrategyType resolveStrategyType( GenericRun run ) {
        if ( run == null || run.getStrategyType() == null || run.getStrategyType().trim().isEmpty() ) {
            return FormationStrategyType.FixedPage;
        }
        return FormationStrategyType.valueOf( run.getStrategyType() );
    }

    protected FormationStrategy createStrategy( FormationStrategyType strategyType, GenericRun run, String claimOwner ) {
        switch ( strategyType ) {
            case WindowedPage:
                return new WindowedPageFormationStrategy64(
                        run.getGuid(),
                        this.mGuidAllocator,
                        this.mMasterManipulator,
                        claimOwner
                );
            case PreemptiveFrame:
                return new PreemptiveFrameFormationStrategy64(
                        run.getGuid(),
                        this.mGuidAllocator,
                        this.mMasterManipulator,
                        claimOwner
                );
            case FixedPage:
            default:
                return new FixedPageFormationStrategy64(
                        run.getGuid(),
                        this.mGuidAllocator,
                        this.mMasterManipulator,
                        claimOwner
                );
        }
    }

    protected GenericFormationStrategyRuntime runtimeOf( GenericRun run, FormationStrategyType strategyType ) {
        GenericFormationStrategyRuntime runtime = new GenericFormationStrategyRuntime();
        runtime.setPageSize( run.getPageSize() );
        runtime.setFrameSize( run.getFrameSize() );
        runtime.setWindowSize( run.getWindowSize() );
        runtime.setInflightLimit( run.getInflightLimit() );
        if ( FormationStrategyType.PreemptiveFrame == strategyType ) {
            runtime.setProductsSum( run.getTotalCount() );
        } else {
            runtime.setProductsSum( ( run.getTotalCount() + run.getPageSize() - 1L ) / Math.max( 1L, run.getPageSize() ) );
        }
        return runtime;
    }
}
