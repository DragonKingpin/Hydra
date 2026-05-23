package com.walnut.odin.proc.server;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.proc.ArchProcessManager;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.proc.ArchRemoteProcessManagerNode;
import com.walnut.odin.proc.RemoteImageResolutionMode;
import com.walnut.odin.proc.ProcessesUtils;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.MediatedRemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.entity.RemoteProcessCreationContext;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlTransportRegistry;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportRegistry;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RavenRemoteProcessManagerServer extends ArchRemoteProcessManagerNode implements RemoteProcessManagerServer {

    protected GuidAllocator                             mGuidAllocator;

    protected RemoteProcessControlTransportRegistry     mTransportRegistry;

    protected Map<Long, Set<GUID>>                      mClientSnapshotProcessMap;

    protected Set<Long>                                 mReadyClientIdSet;

    public RavenRemoteProcessManagerServer( ProcessManager localProcessManager ) {
        super( localProcessManager );
        this.mGuidAllocator             = localProcessManager.getGuidAllocator();
        this.mTransportRegistry         = new GenericRemoteProcessControlTransportRegistry();
        this.mClientSnapshotProcessMap  = new ConcurrentHashMap<>();
        this.mReadyClientIdSet          = ConcurrentHashMap.newKeySet();
    }

    @Override
    public RemoteProcessManagerServer hookTransport( RemoteProcessControlTransport transport ) {
        this.mTransportRegistry.hookTransport( transport );
        return this;
    }

    @Override
    public RemoteProcessControlTransportRegistry transportRegistry() {
        return this.mTransportRegistry;
    }

    @Override
    public Collection<RemoteProcessControlTransport> transports() {
        return this.mTransportRegistry.transports();
    }

    @Override
    public boolean hasClient( long clientId ) {
        return this.mTransportRegistry.hasClient( clientId );
    }

    @Override
    public boolean isControlClientReady( long clientId ) {
        return this.mReadyClientIdSet.contains( clientId );
    }

    @Override
    public Collection<Long> readyControlClientIds() {
        return Collections.unmodifiableCollection( this.mReadyClientIdSet );
    }

    @Override
    public void detachClient( long clientId ) {
        this.mTransportRegistry.detachClient( clientId );
        this.mClientSnapshotProcessMap.remove( clientId );
        this.mReadyClientIdSet.remove( clientId );
        this.expungeClientRemoteProcesses( clientId );
    }

    @Override
    public void registerController( Object controller ) throws RemoteProcessServiceRPCException {
        for ( RemoteProcessControlTransport transport : this.mTransportRegistry.transports() ) {
            transport.registerController( controller );
        }
    }

    @Override
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException {
        for ( RemoteProcessControlTransport transport : this.mTransportRegistry.transports() ) {
            if ( transport.supportsRuntimeIfaceCompile() ) {
                transport.compileIface( ifaceClass, bAsIface );
            }
        }
    }

    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        for ( RemoteProcessControlTransport transport : this.mTransportRegistry.transports() ) {
            transport.startService();
        }
    }

    @Override
    public void terminateService() throws IllegalStateException {
        for ( RemoteProcessControlTransport transport : this.mTransportRegistry.transports() ) {
            transport.terminateService();
        }
    }

    @Override
    public void registerProcess( long clientId, UProcessMirrorDTO processDTO ) {
        RemoteProcess process = this.createMediatedRemoteProcess( clientId, processDTO );
        if ( process == null ) {
            this.getLogger().warn( "[SubordinateRegister] [RegisterProcess (ClientId: {}, PID: {})] <Failure>", clientId, processDTO.getPID() );
            return;
        }

        this.getLogger().info( "[SubordinateRegister] [RegisterProcess (ClientId: {}, PID: {})] <Done>", clientId, processDTO.getPID() );
    }

    @Override
    public void beginClientProcessSnapshot( long clientId ) {
        this.mClientSnapshotProcessMap.put( clientId, ConcurrentHashMap.newKeySet() );
        this.mReadyClientIdSet.remove( clientId );
        this.getLogger().info( "[RemoteProcessControlSnapshot] [Begin] (ClientId: `{}`) <Start>", clientId );
    }

    @Override
    public void acceptClientProcessMirror( long clientId, UProcessMirrorDTO processDTO ) {
        if ( processDTO == null || processDTO.getPID() == null ) {
            this.getLogger().warn( "[RemoteProcessControlSnapshot] [ProcessMirror] (ClientId: `{}`) <Invalid>", clientId );
            return;
        }

        GUID pid = this.mGuidAllocator.parse( processDTO.getPID() );
        Set<GUID> snapshot = this.mClientSnapshotProcessMap.get( clientId );
        if ( snapshot != null ) {
            snapshot.add( pid );
        }

        this.registerProcess( clientId, processDTO );
    }

    @Override
    public void endClientProcessSnapshot( long clientId ) {
        Set<GUID> snapshot = this.mClientSnapshotProcessMap.remove( clientId );
        if ( snapshot == null ) {
            snapshot = new HashSet<>();
        }

        Collection<UProcess> processes = new ArrayList<>( this.mProcessManager.fetchProcesses() );
        for ( UProcess process : processes ) {
            if ( !( process instanceof RemoteProcess ) ) {
                continue;
            }

            RemoteProcess remoteProcess = (RemoteProcess) process;
            if ( remoteProcess.getControlClientId() != clientId ) {
                continue;
            }

            if ( snapshot.contains( remoteProcess.getPID() ) ) {
                continue;
            }

            this.expunge( remoteProcess );
            this.getLogger().info( "[RemoteProcessControlSnapshot] [StaleMirrorExpunged] (ClientId: `{}`, PID: `{}`) <Done>", clientId, remoteProcess.getPID() );
        }

        this.mReadyClientIdSet.add( clientId );
        this.getLogger().info( "[RemoteProcessControlSnapshot] [End] (ClientId: `{}`, MirrorCount: `{}`) <Done>", clientId, snapshot.size() );
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

        this.ensureControlClientReady( clientId );
        this.mTransportRegistry.requireTransport( clientId ).startRemoteUProcess( clientId, pid );
    }

    protected RemoteVitalizationResponse vitalizeRemoteUProcess0(
            long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars,
            boolean directStart
    ) throws RemoteProcessLifecycleException {
        return this.vitalizeRemoteUProcess0(
                clientId,
                RemoteProcessCreationContext.of( imageAddress, isURI, parentPID, startupArgs, contextEnvironmentVars ),
                directStart
        );
    }

    protected RemoteVitalizationResponse vitalizeRemoteUProcess0(
            long clientId, RemoteProcessCreationContext context, boolean directStart
    ) throws RemoteProcessLifecycleException {
        try {
            this.ensureControlClientReady( clientId );
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new RemoteProcessLifecycleException( e );
        }

        UProcessMirrorDTO handlerDTO = new UProcessMirrorDTO();
        String szParentPID = this.mProcessManager.getRootUProcess().getPID().toString();
        if ( context.getParentPID() != null ) {
            szParentPID = context.getParentPID().toString();
        }
        handlerDTO.setParentPID( szParentPID );
        if ( context.getStartupArguments() != null ) {
            handlerDTO.setStartupArguments( JSON.stringify( context.getStartupArguments() ) );
        }
        if ( context.getEnvironmentVariables() != null ) {
            handlerDTO.setEnvironmentVariables( JSON.stringify( context.getEnvironmentVariables() ) );
        }

        handlerDTO.setImageAddress( context.getImageAddress() );
        handlerDTO.setImageAddressURI( context.isImageAddressURI() );
        handlerDTO.setImageResolutionMode( context.getImageResolutionMode() );

        RemoteVitalizationResponse response;
        try {
            RemoteProcessControlTransport transport = this.mTransportRegistry.requireTransport( clientId );
            if ( directStart ) {
                response = transport.vitalizeRemoteUProcess( clientId, handlerDTO );
            }
            else {
                response = transport.createRemoteUProcess( clientId, handlerDTO );
            }
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new RemoteProcessLifecycleException( e );
        }

        if ( response.getPID() != null ) {
            response.setProcessID( this.mGuidAllocator.parse( response.getPID() ) );
        }

        return response;
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
       return this.vitalizeRemoteUProcess( clientId, RemoteProcessCreationContext.of( imageAddress, isURI, parentPID, startupArgs, contextEnvironmentVars ) );
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, RemoteProcessCreationContext context ) throws RemoteProcessLifecycleException {
       return this.vitalizeRemoteUProcess0( clientId, context, true );
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.vitalizeRemoteUProcess( clientId, imagePath, false, parentPID, startupArgs, contextEnvironmentVars );
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.vitalizeRemoteUProcess( clientId, imageURI.toString(), true, parentPID, startupArgs, contextEnvironmentVars );
    }



    @Override
    public RemoteCreationResult createRemoteUProcess( long clientId, String imageAddress, boolean isURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.createRemoteUProcess(
                clientId,
                RemoteProcessCreationContext.of( imageAddress, isURI, parentPID, startupArgs, contextEnvironmentVars )
        );
    }

    @Override
    public RemoteCreationResult createRemoteUProcess( long clientId, RemoteProcessCreationContext context ) throws RemoteProcessLifecycleException {
        RemoteVitalizationResponse response = this.vitalizeRemoteUProcess0( clientId, context, false );
        RemoteCreationResult result = new RemoteCreationResult();
        result.response = response;
        if ( response.getStatus() != RemoteVitalizationStatus.New.getCode() ) {
            return result;
        }

        RemoteProcess remoteProcess = this.createMediatedRemoteProcess( clientId, response );
        if ( remoteProcess != null ) {
            String pid = remoteProcess.getPID().toString();
            this.getLogger().info(
                    "[RemoteProcessCreated] [New::PendingVitalization] [MirrorHooked] (ClientId: `{}`, PID: `{}`) <Done>", clientId, pid
            );
        }
        else {
            this.getLogger().warn(
                    "[RemoteProcessCreated] [New::PendingVitalization] [MirrorHooked] (ClientId: `{}`, ClientProvidedPID: `{}`) <Failure>", clientId, response.getPID()
            );
        }

        result.process  = remoteProcess;
        return result;
    }

    @Override
    public RemoteCreationResult createRemoteUProcess( long clientId, String imagePath, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.createRemoteUProcess( clientId, imagePath, false, parentPID, startupArgs, contextEnvironmentVars );
    }

    @Override
    public RemoteCreationResult createRemoteUProcess( long clientId, URI imageURI, GUID parentPID, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) throws RemoteProcessLifecycleException {
        return this.createRemoteUProcess( clientId, imageURI.toString(), true, parentPID, startupArgs, contextEnvironmentVars );
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

    protected void expungeClientRemoteProcesses( long clientId ) {
        Collection<UProcess> processes = new ArrayList<>( this.mProcessManager.fetchProcesses() );
        for ( UProcess process : processes ) {
            if ( !( process instanceof RemoteProcess ) ) {
                continue;
            }

            RemoteProcess remoteProcess = (RemoteProcess) process;
            if ( remoteProcess.getControlClientId() != clientId ) {
                continue;
            }

            this.expunge( remoteProcess );
            this.getLogger().info( "[RemoteProcessControlClientDetached] [MirrorExpunged] (ClientId: `{}`, PID: `{}`) <Done>", clientId, remoteProcess.getPID() );
        }
    }

    protected void ensureControlClientReady( long clientId ) throws RemoteProcessServiceRPCException {
        RemoteProcessControlTransport transport = this.mTransportRegistry.queryTransport( clientId );
        if ( transport == null || !transport.containsClient( clientId ) ) {
            this.mReadyClientIdSet.remove( clientId );
            throw new RemoteProcessServiceRPCException( "Remote process control client is not connected, clientId => `" + clientId + "`." );
        }

        if ( this.isControlClientReady( clientId ) ) {
            return;
        }

        if ( transport.transportType() == RemoteProcessControlTransportType.Grpc ) {
            return;
        }

        throw new RemoteProcessServiceRPCException( "Remote process control client is not ready, clientId => `" + clientId + "`." );
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

    protected RemoteProcess queryExistingMediatedRemoteProcess( long clientId, GUID pid ) {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process == null ) {
            return null;
        }

        if ( !( process instanceof RemoteProcess ) ) {
            this.getLogger().warn( "[RemoteProcessMirrorConflict] (ClientId: `{}`, PID: `{}`, Process: `{}`) <LocalProcessExists>", clientId, pid, process.getName() );
            return null;
        }

        RemoteProcess remoteProcess = (RemoteProcess) process;
        if ( remoteProcess.getControlClientId() != clientId ) {
            this.getLogger().warn( "[RemoteProcessMirrorConflict] (ClientId: `{}`, PID: `{}`, OwnerClientId: `{}`) <RemoteProcessExists>", clientId, pid, remoteProcess.getControlClientId() );
            return null;
        }

        return remoteProcess;
    }

    protected RemoteProcess createMediatedRemoteProcess(
            long clientId, String name, long localPID, GUID processId,
            String szStartupArguments, String szEnvironmentVariables, String imageAddress, boolean isURI,
            RemoteImageResolutionMode imageResolutionMode
    ) {
        MediatedRemoteProcess process = new MediatedRemoteProcess(
                clientId,this, name, localPID, processId,
                ProcessesUtils.decode( szStartupArguments ), ProcessesUtils.decode( szEnvironmentVariables )
        );

        this.afterMediatedRemoteProcess( process, imageAddress, isURI, imageResolutionMode );
        this.registerProcess( clientId, process );
        return process;
    }

    @Override
    public RemoteProcess createMediatedRemoteProcess( long clientId, RemoteVitalizationResponse response ) {
        GUID pid = this.mGuidAllocator.parse( response.getPID() );
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process != null ) {
            RemoteProcess existingProcess = this.queryExistingMediatedRemoteProcess( clientId, pid );
            return existingProcess;
        }

        return this.createMediatedRemoteProcess(
                clientId, response.getName(), response.getLocalPID(), pid,
                response.getStartupArguments(), response.getEnvironmentVariables(),
                response.getImageAddress(), response.isImageAddressURI(), response.optImageResolutionMode()
        );
    }

    @Override
    public RemoteProcess createMediatedRemoteProcess( long clientId, UProcessMirrorDTO processDTO ) {
        GUID pid = this.mGuidAllocator.parse( processDTO.getPID() );
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process != null ) {
            RemoteProcess existingProcess = this.queryExistingMediatedRemoteProcess( clientId, pid );
            return existingProcess;
        }

        return this.createMediatedRemoteProcess(
                clientId, processDTO.getName(), processDTO.getLocalPID(), pid,
                processDTO.getStartupArguments(), processDTO.getEnvironmentVariables(),
                processDTO.getImageAddress(), processDTO.isImageAddressURI(), processDTO.optImageResolutionMode()
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
                this.ensureControlClientReady( clientId );
                return this.mTransportRegistry.requireTransport( clientId ).queryProcessRuntimeMeta( clientId, pid );
            }

            if ( process == null ) {
                return null;
            }

            UProcessRuntimeMeta meta = ProcessesUtils.extractProcessMeta( process );
            // 涓嶈鐩存帴return 鑰佸瓙濂芥墦鏂偣.
            return meta;
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new RemoteProcessLifecycleException( e );
        }

    }

}

