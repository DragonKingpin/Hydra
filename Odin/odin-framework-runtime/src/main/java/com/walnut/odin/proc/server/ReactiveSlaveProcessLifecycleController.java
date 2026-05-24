package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.entity.RemoteTerminationReport;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;


@Controller
@AddressMapping( "com.walnut.odin.proc.client.SlaveProcessLifecycleIface." )
public class ReactiveSlaveProcessLifecycleController implements Pinenut {

    protected RemoteProcessManagerServer  mRemoteProcessManagerServer;

    public ReactiveSlaveProcessLifecycleController( RemoteProcessManagerServer remoteProcessManagerServer ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
    }

    @AddressMapping( "registerRemoteProcess" )
    public void registerRemoteProcess( long clientId, UProcessMirrorDTO processDTO ) {
        this.mRemoteProcessManagerServer.registerProcess( clientId, processDTO );
    }

    @AddressMapping( "reportProcessTerminated" )
    public void reportProcessTerminated( long clientId, RemoteTerminationReport terminationReport ) {
        if ( terminationReport == null ) {
            this.mRemoteProcessManagerServer.getLogger().warn(
                    "[RemoteProcessTerminated] [RPC] (ClientId: `{}`) <Invalid>", clientId
            );
            return;
        }

        if ( this.mRemoteProcessManagerServer instanceof RavenRemoteProcessManagerServer ) {
            RavenRemoteProcessManagerServer ravenServer = (RavenRemoteProcessManagerServer) this.mRemoteProcessManagerServer;
            RavenRemoteProcessManagerServer.RemoteTerminationAcceptance acceptance = ravenServer.acceptRemoteProcessTermination(
                    clientId, terminationReport
            );
            if ( acceptance.isDuplicate() ) {
                this.mRemoteProcessManagerServer.getLogger().info(
                        "[RemoteProcessTerminated] [RPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Duplicate>",
                        clientId, terminationReport.getPID(), terminationReport.getExitCode()
                );
                return;
            }
            if ( !acceptance.isAccepted() ) {
                this.mRemoteProcessManagerServer.getLogger().warn(
                        "[RemoteProcessTerminated] [RPC] (ClientId: `{}`, PID: `{}`) <Invalid>",
                        clientId, terminationReport.getPID()
                );
                return;
            }

            UProcess process = acceptance.getProcess();
            String procName = "NonExistent";
            if ( process != null ) {
                procName = process.getName();
            }
            this.mRemoteProcessManagerServer.getLogger().info(
                    "[RemoteProcessTerminated] [RPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Done>",
                    clientId, terminationReport.getPID(), terminationReport.getExitCode()
            );
            this.mRemoteProcessManagerServer.getLogger().info(
                    "[RemoteProcessTerminated] [RPC] [MirrorUnhook] (ClientId: `{}`, PID: `{}`, Process: `{}`) <Done>",
                    clientId, terminationReport.getPID(), procName
            );
            return;
        }

        UProcess that = RavenRemoteProcessManagerServer.invokeExpunge( this.mRemoteProcessManagerServer, terminationReport.getPID() );
        String procName = "NonExistent";
        if ( that instanceof RemoteProcess ) {
            procName = that.getName();
            RemoteProcess remoteProcess = (RemoteProcess) that;
            remoteProcess.notifyRemoteEvent( clientId, UProcessStatus.Terminated, terminationReport );
        }
        this.mRemoteProcessManagerServer.getLogger().info(
                "[RemoteProcessTerminated] [RPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Done>",
                clientId, terminationReport.getPID(), terminationReport.getExitCode()
        );
        this.mRemoteProcessManagerServer.getLogger().info(
                "[RemoteProcessTerminated] [RPC] [MirrorUnhook] (ClientId: `{}`, PID: `{}`, Process: `{}`) <Done>", clientId, terminationReport.getPID(), procName
        );
    }

    @AddressMapping( "reportProcessCreated" )
    public String reportProcessCreated( long clientId, RemoteVitalizationResponse vitalizationResponse ) {
        String pid = null;

        RemoteProcess remoteProcess = this.mRemoteProcessManagerServer.createMediatedRemoteProcess( clientId, vitalizationResponse );
        if ( remoteProcess != null ) {
            pid = remoteProcess.getPID().toString();
            this.mRemoteProcessManagerServer.getLogger().info(
                    "[RemoteProcessCreated] [RPC] [MirrorHooked] (ClientId: `{}`, PID: `{}`) <Done>", clientId, pid
            );
        }
        else {
            this.mRemoteProcessManagerServer.getLogger().warn(
                    "[RemoteProcessCreated] [RPC] [MirrorHooked] (ClientId: `{}`, ClientProvidedPID: `{}`) <Failure>", clientId, vitalizationResponse.getPID()
            );
        }
        return pid;
    }

}
