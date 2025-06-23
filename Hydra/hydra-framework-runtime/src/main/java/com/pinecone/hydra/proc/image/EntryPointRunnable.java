package com.pinecone.hydra.proc.image;

import java.util.List;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.hydra.proc.ArchProcessManager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;

public interface EntryPointRunnable extends Runnable, Executor {

    UProcess ownedProcess();

    void applyOwnedProcess( UProcess process );

    ProcessEventHandler processEventHandler();

    void applyProcessEventHandler( ProcessEventHandler handler );

    /**
     * Overriding is discouraged; lifecycle supervision is required in principle.
     * 原则上，请勿重写，需要检察程序生命周期行为。
     */
    @Override
    default void run() {
        ProcessEventHandler processEventHandler        = this.processEventHandler();
        List<ProcessEventHandler> sysProcEventHandlers = ArchEntryPointRunnable.getSysProcEventHandlers( this );
        try {
            ProcessEvent vitalEvent = ProcessEvent.Vitalized;
            if ( processEventHandler != null ) {
                processEventHandler.fired( this, vitalEvent );
            }
            if ( sysProcEventHandlers != null ) {
                for ( ProcessEventHandler sysHandler : sysProcEventHandlers ) {
                    sysHandler.fired( this, vitalEvent );
                }
            }

            this.execute();
        }
        catch ( Exception e ) {
            throw new ProvokeHandleException( e );
        }
        finally {
            UProcess owned = this.ownedProcess();
            ProcessManager processManager = owned.getOwnedProcessManager();
            if ( processManager instanceof ArchProcessManager ) {
                ArchProcessManager.invokeExpunge( (ArchProcessManager) processManager, owned );
            }

            ProcessEvent termEvent = ProcessEvent.Terminated;
            if ( processEventHandler != null ) {
                processEventHandler.fired( this, termEvent );
            }
            if ( sysProcEventHandlers != null ) {
                for ( ProcessEventHandler sysHandler : sysProcEventHandlers ) {
                    sysHandler.fired( this, termEvent );
                }
            }
        }
    }

}
