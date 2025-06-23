package com.walnut.odin.proc.client;

import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.LocalUProcess;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.walnut.odin.proc.ArchRemoteProcessManagerNode;
import com.walnut.odin.proc.ArgumentsUtils;
import com.walnut.odin.proc.ProcessLifecycleExaminer;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessHandlerDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class RavenRemoteProcessManagerClient extends ArchRemoteProcessManagerNode implements RemoteProcessManagerClient {

    protected DuplexAppointClient            mDuplexAppointClient;

    protected SlaveProcessLifecycleIface     mProcessLifecycleIface;

    protected ProcessLifecycleExaminer       mProcessLifecycleExaminer;

    protected long                           mnClientId;

    protected UlfClient                      mRPCClient;

    public RavenRemoteProcessManagerClient( ProcessManager processManager, UlfClient rpcClient ) {
        super( processManager );
        this.mRPCClient          = rpcClient;
        this.mnClientId          = rpcClient.getMessageNodeId();
    }


    protected void initRPCSubsystem() throws RemoteProcessServiceRPCException {
        this.mDuplexAppointClient = new WolvesAppointClient( this.mRPCClient );
        try {
            this.mDuplexAppointClient.compile( SlaveProcessLifecycleIface.class,false );
            this.mProcessLifecycleIface = this.mDuplexAppointClient.getIface( SlaveProcessLifecycleIface.class );
            this.mDuplexAppointClient.getRouteDispatcher().registerController( new ReactiveMasterProcessLifecycleController( this ) );
            this.infoLifecycle( "RPC Subsystem Register Controllers", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            this.mProcessLifecycleIface = null;
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected void vitalizeRPCSubsystem() throws RemoteProcessServiceRPCException {
        try {
            this.mDuplexAppointClient.execute();
            this.mDuplexAppointClient.embraces( 2 );
            this.mProcessLifecycleIface.reportClientInitialized( this.mnClientId );

            this.infoLifecycle( "RPC Subsystem Service Vitalization, ( ClientId: `" + this.mnClientId + "` )", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }


    @Override
    public long getClientId() {
        return this.mnClientId;
    }

    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        this.initRPCSubsystem();
        this.vitalizeRPCSubsystem();
    }

    @Override
    public void terminateService() {
        if ( this.mDuplexAppointClient == null ) {
            throw new IllegalStateException( "RPCClient dose not started yet." );
        }

        this.mDuplexAppointClient.terminate();
    }

    @Override
    public UProcess createLocalUProcess( ExecutionImage image, UProcess parent, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) {
        LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcess( image, parent, startupArgs, contextEnvironmentVars );

        if ( this.mProcessLifecycleIface != null ) {
            UProcessHandlerDTO uProcessHandlerDTO = new UProcessHandlerDTO( localHostedProcess.getName(), localHostedProcess.getLocalPID(), localHostedProcess.getGuid().toString() );
            this.mProcessLifecycleIface.registerRemoteProcess( this.mnClientId, uProcessHandlerDTO );
            this.getLogger().info( "[SuperiorRegister] [createLocalUProcess] <Done>" );
        }
        else {
            this.getLogger().info( "[SuperiorRegister] [createLocalUProcess] <Pass>" ); // Missing central connection, skip reporting; 失联，跳过上报中央.
        }
        return localHostedProcess;
    }

    @Override
    public RemoteVitalizationResponse vitalizeLocalUProcess( String imageAddress, boolean isURI, UProcessHandlerDTO handlerDTO ) throws RemoteProcessLifecycleException {
        try {
            RemoteVitalizationResponse response = new RemoteVitalizationResponse();

            ExecutionImage image;
            if ( isURI ) {
                URI uri = new URI( imageAddress );
                image = this.queryExecutionImage( uri );
            }
            else {
                image = this.queryExecutionImage( imageAddress );
            }

            if ( image == null ) {
                response.setRemoteVitalizationStatus( RemoteVitalizationStatus.NoImage );
                return response;
            }

            String szStartupArguments      = handlerDTO.getStartupArguments();
            String szEnvironmentVariables  = handlerDTO.getEnvironmentVariables();
            String szParentPID             = handlerDTO.getParentPID();

            Map<String, String[]> startupArgs  = ArgumentsUtils.decode( szStartupArguments );
            Map<String, String[]> envVariables = ArgumentsUtils.decode( szEnvironmentVariables );
            GUID parentPID = null;
            if ( szParentPID != null ) {
                parentPID = this.mProcessManager.getGuidAllocator().parse( szParentPID );
            }

            LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcess( image, this.mProcessManager.getRootUProcess(), startupArgs, envVariables );
            localHostedProcess.applyActualParentPID( parentPID );
            localHostedProcess.start(); // TODO, Process Joint

            response.setProcessID( localHostedProcess.getPID() );
            response.setLocalPID( localHostedProcess.getLocalPID() );

            return response;
        }
        catch ( URISyntaxException e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public void startLocalUProcess( GUID pid ) throws IllegalArgumentException {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process == null ) {
            throw new IllegalArgumentException( "No such process, PID => `" + pid + "`" );
        }
        process.start();
    }

}
