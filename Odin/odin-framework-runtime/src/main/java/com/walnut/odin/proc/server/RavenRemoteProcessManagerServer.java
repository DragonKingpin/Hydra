package com.walnut.odin.proc.server;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.RuntimeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.image.kom.VirtualExeImageInstrument;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.walnut.odin.proc.ArchRemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RavenRemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessHandlerDTO;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RavenRemoteProcessManagerServer extends ArchRemoteProcessManagerNode implements RemoteProcessManagerServer {

    protected GuidAllocator                             mGuidAllocator;

    protected Map<Long, RemoteProcess>                  mRemoteProcessMap;

    protected Map<GUID, Long>                           mPidClientIdMap;

    protected Map<Long, MasterProcessLifecycleIface>    mLifecycleIfaceCMap;

    protected UlfServer                                 mRPCServer;

    protected DuplexAppointServer                       mDuplexAppointServer;

    public RavenRemoteProcessManagerServer( ProcessManager localProcessManager, UlfServer ulfServer ) {
        super( localProcessManager );
        this.mRemoteProcessMap      = new ConcurrentHashMap<>();
        this.mPidClientIdMap        = new ConcurrentHashMap<>();
        this.mLifecycleIfaceCMap    = new ConcurrentHashMap<>();
        this.mGuidAllocator         = localProcessManager.getGuidAllocator();
        this.mRPCServer             = ulfServer;
    }

    protected void initRPCSubsystem() throws RemoteProcessServiceRPCException {
        try {
            this.mDuplexAppointServer = new WolvesAppointServer( this.mRPCServer, HuskyDuplexExpress.class );
            ReactiveSlaveProcessLifecycleController controller = new ReactiveSlaveProcessLifecycleController( this );
            this.mDuplexAppointServer.registerController( controller );
            this.mDuplexAppointServer.compile( MasterProcessLifecycleIface.class, false );

            this.infoLifecycle( "RPC Subsystem Register Controllers", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected void vitalizeRPCSubsystem() throws RemoteProcessServiceRPCException {
        try {
            this.mDuplexAppointServer.execute();
            this.infoLifecycle( "RPC Subsystem Service Vitalization", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }


    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        this.initRPCSubsystem();
        this.vitalizeRPCSubsystem();
    }

    @Override
    public void terminateService() throws IllegalStateException {
        if ( this.mDuplexAppointServer == null ) {
            throw new IllegalStateException( "RPCServer dose not started yet." );
        }

        this.mDuplexAppointServer.terminate();
    }

    @Override
    public void registerProcess( long clientId, UProcessHandlerDTO processDTO ) {
        String name = processDTO.getName();
        GUID pid = this.mGuidAllocator.parse( processDTO.getPID() );
        long localPID = processDTO.getLocalPID();
        String startupArguments = processDTO.getStartupArguments();
        String environmentVariables = processDTO.getEnvironmentVariables();

        RemoteProcess remoteProcess = new RavenRemoteProcess( this, name, localPID, pid );
        this.mRemoteProcessMap.put( clientId, remoteProcess );
        this.mPidClientIdMap.put( pid, clientId );

        this.getLogger().info( "[SubordinateRegister] [registerProcess (ClientId: {}, PID: {})] <Done>", clientId, pid );
    }

    @Override
    public void startRemoteUProcess( GUID pid ) throws RemoteProcessServiceRPCException {
        Long clientId = this.mPidClientIdMap.get( pid );
        if ( clientId == null ) {
            throw new IllegalArgumentException( "No such process, PID => `" + pid + "`" );
        }

        try {
            this.mDuplexAppointServer.invokeInform( clientId, "com.walnut.odin.proc.server.MasterProcessLifecycleIface.startRemoteUProcess", pid );
        }
        catch ( IOException e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        UProcessHandlerDTO handlerDTO = new UProcessHandlerDTO();
        handlerDTO.setParentPID( parentPID.toString() );
        if ( startupArgs != null ) {
            handlerDTO.setStartupArguments( JSON.stringify( startupArgs ) );
        }
        if ( contextEnvironmentVars != null ) {
            handlerDTO.setEnvironmentVariables( JSON.stringify( contextEnvironmentVars ) );
        }

        try {
            Object ret = this.mDuplexAppointServer.invokeInform(
                    clientId, "com.walnut.odin.proc.server.MasterProcessLifecycleIface.vitalizeRemoteUProcess",
                    imageAddress, isURI, handlerDTO
            );

            RemoteVitalizationResponse response = (RemoteVitalizationResponse) ret;
            if ( response.getPID() != null ) {
                response.setProcessID( this.mGuidAllocator.parse( response.getPID() ) );
            }

            return response;
        }
        catch ( IOException e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.vitalizeRemoteUProcess( clientId, imagePath, false, parentPID, startupArgs, contextEnvironmentVars );
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.vitalizeRemoteUProcess( clientId, imageURI.toString(), true, parentPID, startupArgs, contextEnvironmentVars );
    }
}
