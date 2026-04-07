package com.walnut.odin.dispatch;

import java.util.Collection;
import java.util.Collections;

import com.pinecone.hydra.proc.UProcess;

public class DefaultPipelineLaunchReport implements PipelineLaunchReport {

    protected TaskExecutionProcessor          mProcessor;
    protected Collection<UProcess>            mLaunchedProcesses;
    protected Collection<TaskLaunchContext>   mLaunchedContext;
    protected Collection<TaskLaunchContext>   mWaitingContext;
    protected boolean                         mbPreparing;

    protected DefaultPipelineLaunchReport(
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

    public static DefaultPipelineLaunchReport preparing(
            TaskExecutionProcessor processor,
            Collection<UProcess> launchedProcesses,
            Collection<TaskLaunchContext> waitingContext
    ) {
        return new DefaultPipelineLaunchReport(
                processor,
                launchedProcesses,
                Collections.emptyList(),
                waitingContext,
                true
        );
    }

    public static DefaultPipelineLaunchReport executed(
            TaskExecutionProcessor processor,
            Collection<UProcess> launchedProcesses,
            Collection<TaskLaunchContext> launchedContext,
            Collection<TaskLaunchContext> waitingContext
    ) {
        return new DefaultPipelineLaunchReport(
                processor,
                launchedProcesses,
                launchedContext,
                waitingContext,
                false
        );
    }

    public static DefaultPipelineLaunchReport recycled(
            TaskExecutionProcessor processor,
            Collection<TaskLaunchContext> recycled
    ) {
        return new DefaultPipelineLaunchReport(
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