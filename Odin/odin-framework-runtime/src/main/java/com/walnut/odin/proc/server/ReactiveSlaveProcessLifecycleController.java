package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
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

    @AddressMapping( "reportClientInitialized" )
    public long reportClientInitialized( long clientId ) {
        this.mRemoteProcessManagerServer.getLogger().info( "[ClientInitializedRecall] [RPC] (ClientId: `{}`) <Done>", clientId );
        return clientId;
    }

    @AddressMapping( "reportProcessTerminated" )
    public void reportProcessTerminated( long clientId, RemoteTerminationReport terminationReport ) {
        this.mRemoteProcessManagerServer.getLogger().info(
                "[RemoteProcessTerminated] [RPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Done>", clientId, terminationReport.getPID(), terminationReport.getExitCode()
        );

        UProcess that = RavenRemoteProcessManagerServer.invokeExpunge( this.mRemoteProcessManagerServer, terminationReport.getPID() );
        String procName = "NonExistent";
        if ( that != null ) {
            procName = that.getName();
            RemoteProcess remoteProcess = (RemoteProcess) that;
            remoteProcess.notifyRemoteEvent( clientId, ProcessEvent.Terminated, terminationReport );
        }

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
