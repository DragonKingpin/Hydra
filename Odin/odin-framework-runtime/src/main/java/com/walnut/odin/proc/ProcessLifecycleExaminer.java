package com.walnut.odin.proc;

import com.pinecone.framework.system.regime.Examiner;
import com.pinecone.hydra.proc.UProcess;

/**
 * ProcessLifecycleExaminer
 * Process Lifecycle Manager and Runtime Examiner
 * Manages process lifecycles, action auditing, and runtime inspection.
 * 进程生命周期管理与运行检察器，管理进程生命周期与行为审计、检查
 */
public interface ProcessLifecycleExaminer extends Examiner {

    void startProcess( UProcess process );

}
