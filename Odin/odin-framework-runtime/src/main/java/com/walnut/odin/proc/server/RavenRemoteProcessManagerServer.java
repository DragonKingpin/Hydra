package com.walnut.odin.proc.server;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.proc.ArchProcessManager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.walnut.odin.proc.ArchRemoteProcessManagerNode;
import com.walnut.odin.proc.ProcessesUtils;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.MediatedRemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;
import com.walnut.odin.proc.dto.UProcessRuntimeMeta;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RavenRemoteProcessManagerServer extends ArchRemoteProcessManagerNode implements RemoteProcessManagerServer {

    protected GuidAllocator                             mGuidAllocator;

    //protected Map<GUID, Long>                           mPidClientIdMap;

    protected Map<Long, MasterProcessLifecycleIface>    mLifecycleIfaceCMap;

    protected UlfServer                                 mRPCServer;

    protected DuplexAppointServer                       mDuplexAppointServer;

    public RavenRemoteProcessManagerServer( ProcessManager localProcessManager, UlfServer ulfServer ) {
        super( localProcessManager );
        //this.mPidClientIdMap        = new ConcurrentHashMap<>();
        this.mLifecycleIfaceCMap    = new ConcurrentHashMap<>();
        this.mGuidAllocator         = localProcessManager.getGuidAllocator();
        this.mRPCServer             = ulfServer;
    }

    protected void initRPCSubsystem() throws RemoteProcessServiceRPCException {
        if ( this.mDuplexAppointServer != null && !this.mDuplexAppointServer.getMessageNode().isTerminated() ) {
            throw new IllegalStateException( "DuplexAppointServer has started." );
        }

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
        this.mDuplexAppointServer = null;
    }

    @Override
    public void registerProcess( long clientId, UProcessMirrorDTO processDTO ) {
        this.createMediatedRemoteProcess( clientId, processDTO );

        this.getLogger().info( "[SubordinateRegister] [RegisterProcess (ClientId: {}, PID: {})] <Done>", clientId, processDTO.getPID() );
    }

    @Override
    public void startRemoteUProcess( GUID pid ) throws RemoteProcessServiceRPCException {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process == null  ) {
            throw new IllegalArgumentException( "No such process, PID => `" + pid + "`" );
        }
        if ( !( process instanceof RemoteProcess )  ) {
            throw new IllegalArgumentException( "{Target process is not remote process, PID => `" + pid + "`" );
        }

        RemoteProcess rp = (RemoteProcess) process;
        long clientId = rp.getControlClientId();

        try {
            this.mDuplexAppointServer.invokeInform( clientId, "com.walnut.odin.proc.server.MasterProcessLifecycleIface.startRemoteUProcess", pid );
        }
        catch ( IOException e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String[]> startupArgs, Map<String, String[]> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        UProcessMirrorDTO handlerDTO = new UProcessMirrorDTO();
        handlerDTO.setParentPID( parentPID.toString() );
        if ( startupArgs != null ) {
            handlerDTO.setStartupArguments( JSON.stringify( startupArgs ) );
        }
        if ( contextEnvironmentVars != null ) {
            handlerDTO.setEnvironmentVariables( JSON.stringify( contextEnvironmentVars ) );
        }

        handlerDTO.setImageAddress( imageAddress );
        handlerDTO.setImageAddressURI( isURI );

        try {
            Object ret = this.mDuplexAppointServer.invokeInform(
                    clientId, "com.walnut.odin.proc.server.MasterProcessLifecycleIface.vitalizeRemoteUProcess",
                    handlerDTO
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

    @Override
    public void register( UProcess that ) {
        this.mProcessManager.register( that );
    }

    @Override
    public Long queryClientIdByPID( GUID pid ) {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process instanceof RemoteProcess ) {
            RemoteProcess rp = (RemoteProcess) process;
            return rp.getControlClientId();
        }
        return null;
    }

    protected void expungeSelf( GUID pid ) {
        // this.mPidClientIdMap.remove( pid );

        // Reserved.
    }

    @Override
    public void erase( UProcess that ) {
        this.mProcessManager.erase( that );
        this.expungeSelf( that.getPID() );
    }

    protected void expunge( UProcess that ) {
        ArchProcessManager.invokeExpunge( this.mProcessManager, that );
        this.expungeSelf( that.getPID() );
    }

    protected UProcess expunge( GUID pid ) {
        UProcess that = this.mProcessManager.getProcess( pid );
        if ( that != null ) {
            this.expunge( that );
        }
        return that;
    }

    protected void registerProcess( long clientId, RemoteProcess process ) {
        //this.mPidClientIdMap.put( process.getPID(), clientId );
        this.register( process );
    }

    protected RemoteProcess createMediatedRemoteProcess(
            long clientId, String name, long localPID, GUID processId,
            String szStartupArguments, String szEnvironmentVariables, String imageAddress, boolean isURI
    ) {
        MediatedRemoteProcess process = new MediatedRemoteProcess(
                clientId,this, name, localPID, processId,
                ProcessesUtils.decode( szStartupArguments ), ProcessesUtils.decode( szEnvironmentVariables )
        );

        this.afterMediatedRemoteProcess( process, imageAddress, isURI );
        this.registerProcess( clientId, process );
        return process;
    }

    @Override
    public RemoteProcess createMediatedRemoteProcess( long clientId, RemoteVitalizationResponse response ) {
        return this.createMediatedRemoteProcess(
                clientId, response.getName(), response.getLocalPID(), this.mGuidAllocator.parse( response.getPID() ),
                response.getStartupArguments(), response.getEnvironmentVariables(),
                response.getImageAddress(), response.isImageAddressURI()
        );
    }

    @Override
    public RemoteProcess createMediatedRemoteProcess( long clientId, UProcessMirrorDTO processDTO ) {
        return this.createMediatedRemoteProcess(
                clientId, processDTO.getName(), processDTO.getLocalPID(), this.mGuidAllocator.parse( processDTO.getPID() ),
                processDTO.getStartupArguments(), processDTO.getEnvironmentVariables(),
                processDTO.getImageAddress(), processDTO.isImageAddressURI()
        );
    }

    public static UProcess invokeExpunge( RemoteProcessManagerServer server, String pid ) {
        if ( server instanceof RavenRemoteProcessManagerServer ) {
            RavenRemoteProcessManagerServer ravenServer = (RavenRemoteProcessManagerServer) server;
            return ravenServer.expunge( ravenServer.mGuidAllocator.parse( pid ) );
        }
        return null;
    }

    @Override
    public UProcessRuntimeMeta queryProcessRuntimeMeta( GUID pid ) throws RemoteProcessLifecycleException {
        try {
            UProcess process = this.mProcessManager.getProcess( pid );
            if ( process instanceof RemoteProcess ) {
                RemoteProcess remoteProcess = (RemoteProcess) process;
                long clientId = remoteProcess.getControlClientId();
                Object ret = this.mDuplexAppointServer.invokeInform(
                        clientId, "com.walnut.odin.proc.server.MasterProcessLifecycleIface.queryRemoteProcessRuntimeMeta",
                        pid.toString()
                );
                return (UProcessRuntimeMeta) ret; // Cascading retrieval of runtime meta information
            }

            if ( process == null ) {
                return null;
            }

            UProcessRuntimeMeta meta = ProcessesUtils.extractProcessMeta( process );
            // 不要直接return 老子好打断点.
            return meta;
        }
        catch ( IOException e ) {
            throw new RemoteProcessLifecycleException( e );
        }

    }

}
