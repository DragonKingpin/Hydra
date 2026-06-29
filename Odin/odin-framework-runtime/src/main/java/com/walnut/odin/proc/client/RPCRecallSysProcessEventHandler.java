package com.walnut.odin.proc.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.walnut.odin.proc.RemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteTerminationStatus;
import com.walnut.odin.proc.entity.RemoteTerminationReport;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RPCRecallSysProcessEventHandler implements ProcessEventHandler {

    protected RemoteProcessManagerNode     mRemoteProcessManagerNode;
    protected SlaveProcessLifecycleIface   mSlaveProcessLifecycleIface;
    protected long                         mnClientId;
    protected Set<GUID>                    mReportedTerminatedProcessIds;

    public RPCRecallSysProcessEventHandler( long clientId, RemoteProcessManagerNode node, SlaveProcessLifecycleIface iface ) {
        this.mRemoteProcessManagerNode   = node;
        this.mSlaveProcessLifecycleIface = iface;
        this.mnClientId                  = clientId;
        this.mReportedTerminatedProcessIds = ConcurrentHashMap.newKeySet();
    }

    public RPCRecallSysProcessEventHandler( RemoteProcessManagerNode node, SlaveProcessLifecycleIface iface ) {
        this( -1, node, iface );

        if ( node instanceof RemoteProcessManagerClient ) {
            this.mnClientId = ((RemoteProcessManagerClient) node).getClientId();
        }
    }

    @Override
    public void fired( EntryPointRunnable runnable, UProcessStatus status ) {
        switch ( status ) {
            case Terminated: {
                this.notifyProcessTerminated( runnable );
                break;
            }
            case Error: {
                this.notifyProcessTerminated( runnable );
                break;
            }
            case Preparing:
            case Created:
            case Activated:
            default: {
                break;
            }
        }
    }

    protected void notifyProcessTerminated( EntryPointRunnable runnable ) {
        UProcess process = runnable.ownedProcess();
        if ( process == null || !this.mReportedTerminatedProcessIds.add( process.getPID() ) ) {
            return;
        }

        RemoteTerminationReport report = new RemoteTerminationReport();
        report.setProcessID( process.getPID() );
        report.setExitCode( process.actionTape().getExitCode() );
        report.setLocalPID( process.getLocalPID() );
        report.setRemoteTerminationStatus( RemoteTerminationStatus.Expected );

        Throwable lastError = process.actionTape().getLastError();
        RemoteTerminationStatus signalStatus = this.consumeSignalTerminationStatus( process );
        if ( signalStatus != null ) {
            if ( lastError != null ) {
                report.setErrorMsg( lastError.getMessage() );
            }
            report.setRemoteTerminationStatus( signalStatus );
            this.mRemoteProcessManagerNode.notifyProcessLifecycleHandlers(
                    process.getExecutionImage().getImageAddress(), process.getEntryPoint(), UProcessStatus.Terminated
            );
        }
        else if ( lastError != null ) {
            report.setErrorMsg( lastError.getMessage() );
            report.setRemoteTerminationStatus( RemoteTerminationStatus.Error );
            this.mRemoteProcessManagerNode.notifyProcessLifecycleHandlers(
                    process.getExecutionImage().getImageAddress(), process.getEntryPoint(), UProcessStatus.Error
            );
        }
        else {
            this.mRemoteProcessManagerNode.notifyProcessLifecycleHandlers(
                    process.getExecutionImage().getImageAddress(), process.getEntryPoint(), UProcessStatus.Terminated
            );
        }

        this.mSlaveProcessLifecycleIface.reportProcessTerminated( this.mnClientId, report );
    }

    protected RemoteTerminationStatus consumeSignalTerminationStatus( UProcess process ) {
        if ( !( this.mRemoteProcessManagerNode instanceof RemoteProcessManagerClient ) ) {
            return null;
        }
        return ( (RemoteProcessManagerClient)this.mRemoteProcessManagerNode ).consumeSignalTerminationStatus( process.getPID() );
    }
}
