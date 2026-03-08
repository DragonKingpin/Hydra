package com.walnut.odin.dispatch;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;

public interface PipelineElevationReport extends Pinenut {

    Collection<UProcess> launchedProcesses();

    Collection<TaskLaunchContext> launchedContext();

    Collection<TaskLaunchContext> waitingContext();

    boolean isPreparing();

}
