package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.functions.Executor;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;

public interface EntryPointRunnable extends Runnable, Executor {

    UProcess ownedProcess();

    void applyOwnedProcess( UProcess process );

    /**
     * Overriding is discouraged; lifecycle supervision is required in principle.
     * 原则上，请勿重写，需要检察程序生命周期行为。
     * @throws Exception;
     */
    @Override
    default void execute() throws Exception {
        try {
            this.run();
        }
        finally {
            UProcess owned = this.ownedProcess();
            ProcessManager processManager = owned.getOwnedProcessManager();
            processManager.erase( owned );
        }
    }

}
