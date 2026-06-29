package com.pinecone.hydra.proc.image;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;

public abstract class ArchEntryPointRunnable implements EntryPointRunnable {

    protected UProcess mOwnedProcess;

    protected ProcessEventHandler mProcessEventHandler;

    List<ProcessEventHandler> mSysProcEventHandlers;

    public ArchEntryPointRunnable( UProcess ownedProcess, ProcessEventHandler processEventHandler ) {
        this.mOwnedProcess         = ownedProcess;
        this.mProcessEventHandler  = processEventHandler;
        this.mSysProcEventHandlers = new ArrayList<>();
    }

    public ArchEntryPointRunnable( ProcessEventHandler processEventHandler ) {
        this( null, processEventHandler );
    }

    public ArchEntryPointRunnable() {
        this( null, null );
    }

    @Override
    public ProcessEventHandler processEventHandler() {
        return this.mProcessEventHandler;
    }

    @Override
    public void applyProcessEventHandler( ProcessEventHandler handler ) {
        if ( this.mOwnedProcess != null && this.mOwnedProcess.getStatus() != UProcessStatus.Created && this.mOwnedProcess.getStatus() != UProcessStatus.Registered ) {
            throw new IllegalStateException(
                    "Process event handler can only be set before the process is started."
            );
        }
        this.mProcessEventHandler = handler;
    }

    @Override
    public UProcess ownedProcess() {
        return this.mOwnedProcess;
    }

    @Override
    public void applyOwnedProcess( UProcess process ) {
        this.mOwnedProcess = process;
    }

    static List<ProcessEventHandler> getSysProcEventHandlers( EntryPointRunnable entryPointRunnable ) {
        if ( entryPointRunnable instanceof ArchEntryPointRunnable ) {
            return ((ArchEntryPointRunnable) entryPointRunnable).mSysProcEventHandlers;
        }
        else {
            return null;
        }
    }
}
