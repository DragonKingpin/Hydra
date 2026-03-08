package com.walnut.odin.dispatch;

import java.util.Collection;
import java.util.Collections;

import com.pinecone.hydra.proc.UProcess;

public class DefaultPipelineElevationReport implements PipelineElevationReport {

    protected TaskExecutionProcessor          mProcessor;
    protected Collection<UProcess>            mLaunchedProcesses;
    protected Collection<TaskLaunchContext>   mLaunchedContext;
    protected Collection<TaskLaunchContext>   mWaitingContext;
    protected boolean                         mbPreparing;

    protected DefaultPipelineElevationReport(
            TaskExecutionProcessor processor,
            Collection<UProcess> launchedProcesses,
            Collection<TaskLaunchContext> launchedContext,
            Collection<TaskLaunchContext> waitingContext,
            boolean preparing
    ) {
        this.mProcessor          = processor;
        this.mLaunchedProcesses  = launchedProcesses;
        this.mLaunchedContext    = launchedContext;
        this.mWaitingContext     = waitingContext;
        this.mbPreparing         = preparing;
    }

    public static DefaultPipelineElevationReport preparing(
            TaskExecutionProcessor processor,
            Collection<UProcess> launchedProcesses,
            Collection<TaskLaunchContext> waitingContext
    ) {
        return new DefaultPipelineElevationReport(
                processor,
                launchedProcesses,
                Collections.emptyList(),
                waitingContext,
                true
        );
    }

    public static DefaultPipelineElevationReport executed(
            TaskExecutionProcessor processor,
            Collection<UProcess> launchedProcesses,
            Collection<TaskLaunchContext> launchedContext,
            Collection<TaskLaunchContext> waitingContext
    ) {
        return new DefaultPipelineElevationReport(
                processor,
                launchedProcesses,
                launchedContext,
                waitingContext,
                false
        );
    }

    public static DefaultPipelineElevationReport recycled(
            TaskExecutionProcessor processor,
            Collection<TaskLaunchContext> recycled
    ) {
        return new DefaultPipelineElevationReport(
                processor,
                Collections.emptyList(),
                Collections.emptyList(),
                recycled,
                false
        );
    }

    @Override
    public Collection<UProcess> launchedProcesses() {
        return this.mLaunchedProcesses;
    }

    @Override
    public Collection<TaskLaunchContext> launchedContext() {
        return this.mLaunchedContext;
    }

    @Override
    public Collection<TaskLaunchContext> waitingContext() {
        return this.mWaitingContext;
    }

    @Override
    public boolean isPreparing() {
        return this.mbPreparing;
    }
}