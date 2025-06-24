package com.walnut.odin.proc.client;

import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteTerminationStatus;
import com.walnut.odin.proc.dto.RemoteTerminationReport;

public class RPCRecallSysProcessEventHandler implements ProcessEventHandler {

    protected RemoteProcessManagerNode     mRemoteProcessManagerNode;
    protected SlaveProcessLifecycleIface   mSlaveProcessLifecycleIface;
    protected long                         mnClientId;

    public RPCRecallSysProcessEventHandler( long clientId, RemoteProcessManagerNode node, SlaveProcessLifecycleIface iface ) {
        this.mRemoteProcessManagerNode   = node;
        this.mSlaveProcessLifecycleIface = iface;
        this.mnClientId                  = clientId;
    }

    public RPCRecallSysProcessEventHandler( RemoteProcessManagerNode node, SlaveProcessLifecycleIface iface ) {
        this( -1, node, iface );

        if ( node instanceof RemoteProcessManagerClient ) {
            this.mnClientId = ((RemoteProcessManagerClient) node).getClientId();
        }
    }

    @Override
    public void fired( EntryPointRunnable runnable, ProcessEvent event ) {
        switch ( event ) {
            case Terminated: {
                this.notifyProcessTerminated( runnable );
                break;
            }
            case Created:
            case Vitalized:
            default: {
                break;
            }
        }
    }

    protected void notifyProcessTerminated( EntryPointRunnable runnable ) {
        UProcess process = runnable.ownedProcess();
        RemoteTerminationReport report = new RemoteTerminationReport();
        report.setProcessID( process.getPID() );
        report.setExitCode( process.actionTape().getExitCode() );
        report.setLocalPID( process.getLocalPID() );
        report.setRemoteTerminationStatus( RemoteTerminationStatus.Expected );

        Throwable lastError = process.actionTape().getLastError();
        if ( lastError != null ) {
            report.setErrorMsg( lastError.getMessage() );
            report.setRemoteTerminationStatus( RemoteTerminationStatus.Error );
        }

        this.mSlaveProcessLifecycleIface.reportProcessTerminated( this.mnClientId, report );
    }
}
