package com.pinecone.hydra.proc.image;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.functions.Executor;
import com.pinecone.hydra.proc.ArchProcessManager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;

public interface EntryPointRunnable extends Runnable, Executor {

    UProcess ownedProcess();

    void applyOwnedProcess( UProcess process );

    ProcessEventHandler processEventHandler();

    void applyProcessEventHandler( ProcessEventHandler handler );

    int main( Map<String, String[]> args ) throws Exception;

    @Override
    default void execute() throws Exception {
        int c = this.main( this.ownedProcess().getStartupArguments() );
        this.ownedProcess().actionTape().setExitCode( c );
    }

    /**
     * Overriding is discouraged; lifecycle supervision is required in principle.
     * 原则上，请勿重写，需要检察程序生命周期行为。
     */
    @Override
    default void run() {
        ProcessEventHandler processEventHandler        = this.processEventHandler();
        List<ProcessEventHandler> sysProcEventHandlers = ArchEntryPointRunnable.getSysProcEventHandlers( this );
        UProcessStatus terminalStatus                  = null;
        try {
            this.fireStatus( processEventHandler, sysProcEventHandlers, UProcessStatus.Running );

            int c = this.main( this.ownedProcess().getStartupArguments() );
            this.ownedProcess().actionTape().setExitCode( c );
        }
        catch ( Exception e ) {
            this.ownedProcess().actionTape().setLastError( e );
            terminalStatus = UProcessStatus.Error;
            throw new ProvokeHandleException( e );
        }
        finally {
            UProcess owned = this.ownedProcess();
            if ( terminalStatus == null ) {
                terminalStatus = UProcessStatus.Terminated;
            }
            owned.applyStatus( terminalStatus );
            ProcessManager processManager = owned.getOwnedProcessManager();
            if ( processManager instanceof ArchProcessManager ) {
                ArchProcessManager.invokeExpunge( (ArchProcessManager) processManager, owned );
            }

            this.fireStatus( processEventHandler, sysProcEventHandlers, terminalStatus );
        }
    }

    default void fireStatus( ProcessEventHandler processEventHandler, List<ProcessEventHandler> sysProcEventHandlers, UProcessStatus status ) {
        UProcess owned = this.ownedProcess();
        if ( owned != null ) {
            owned.applyStatus( status );
        }
        if ( processEventHandler != null ) {
            processEventHandler.fired( this, status );
        }
        if ( sysProcEventHandlers != null ) {
            for ( ProcessEventHandler sysHandler : sysProcEventHandlers ) {
                sysHandler.fired( this, status );
            }
        }
    }

}
