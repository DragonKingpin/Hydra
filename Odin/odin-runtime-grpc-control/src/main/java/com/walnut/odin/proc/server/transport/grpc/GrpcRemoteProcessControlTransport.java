package com.walnut.odin.proc.server.transport.grpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlSession;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;
import com.walnut.odin.proc.server.transport.entity.TransportConnection;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.CommandResult;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrameType;

public class GrpcRemoteProcessControlTransport implements RemoteProcessControlTransport {

    protected Logger                            log = LoggerFactory.getLogger( this.getClass() );

    protected RemoteProcessManagerServer        mRemoteProcessManagerServer;

    protected GuidAllocator                     mGuidAllocator;

    protected GrpcAppointServer                 mGrpcAppointServer;

    protected RemoteProcessControlEventHooker   mEventHooker;

    protected Map<Long, GrpcRemoteProcessControlClientile> mClientileMap;

    protected GrpcCorrelationWaiter<Object>     mCorrelationWaiter;

    protected GrpcRemoteProcessFrameMapper      mFrameMapper;

    protected Object                            mProcessorLifecycleController;

    public GrpcRemoteProcessControlTransport( RemoteProcessManagerServer remoteProcessManagerServer,
                                              GrpcAppointServer grpcAppointServer,
                                              RemoteProcessControlEventHooker eventHooker ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
        this.mGuidAllocator              = remoteProcessManagerServer.getGuidAllocator();
        this.mGrpcAppointServer          = grpcAppointServer;
        this.mEventHooker                = eventHooker;
        this.mClientileMap               = new ConcurrentHashMap<>();
        this.mCorrelationWaiter          = new GrpcCorrelationWaiter<>();
        this.mFrameMapper                = new GrpcRemoteProcessFrameMapper( this.mGuidAllocator );
    }

    public RemoteProcessManagerServer remoteProcessManagerServer() {
        return this.mRemoteProcessManagerServer;
    }

    public GrpcAppointServer grpcAppointServer() {
        return this.mGrpcAppointServer;
    }

    public GrpcCorrelationWaiter<Object> correlationWaiter() {
        return this.mCorrelationWaiter;
    }

    public GrpcRemoteProcessFrameMapper frameMapper() {
        return this.mFrameMapper;
    }

    protected String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    public void bindClientSession( GrpcRemoteProcessControlSession session ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.computeIfAbsent(
                session.clientId(),
                clientId -> new GrpcRemoteProcessControlClientile( clientId, this )
        );
        clientile.attachSession( session );
        if ( this.mEventHooker != null ) {
            this.mEventHooker.onClientInitialized( this, session.clientId() );
        }
    }

    public void detachClientSession( GrpcRemoteProcessControlSession session ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( session.clientId() );
        if ( clientile != null ) {
            clientile.detachSession( session );
            if ( !clientile.isActive() ) {
                this.mClientileMap.remove( session.clientId() );
                if ( this.mEventHooker != null ) {
                    this.mEventHooker.onClientDetached( this, session.clientId() );
                }
            }
        }
        session.close();
    }

    @Override
    public RemoteProcessControlTransportType transportType() {
        return RemoteProcessControlTransportType.Grpc;
    }

    @Override
    public boolean containsClient( long clientId ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( clientId );
        return clientile != null && clientile.isActive();
    }

    @Override
    public Collection<TransportConnection> queryClientConnections( long clientId ) {
        List<TransportConnection> connections = new ArrayList<>();
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( clientId );
        if ( clientile == null ) {
            return connections;
        }

        for ( RemoteProcessControlSession session : clientile.sessions() ) {
            TransportConnection connection = new TransportConnection();
            connection.setType( "Session" );
            if ( session instanceof GrpcRemoteProcessControlSession ) {
                connection.setIdentity( ( (GrpcRemoteProcessControlSession) session ).sessionGuid() );
            }
            else {
                connection.setIdentity( session.getClass().getSimpleName() + "@" + System.identityHashCode( session ) );
            }
            connection.setStatus( session.isActive() ? "ACTIVE" : "INACTIVE" );
            connection.setActive( session.isActive() );
            connections.add( connection );
        }
        return connections;
    }

    public void acceptClientFrame( GrpcRemoteProcessControlSession session, RemoteProcessControlFrame frame ) {
        String szCorrelationGuid = frame.getCorrelationGuid();
        switch ( frame.getFrameType() ) {
            case COMMAND_RESULT: {
                this.mCorrelationWaiter.complete( szCorrelationGuid, frame.getCommandResult() );
                break;
            }
            case PROCESS_RUNTIME_META: {
                this.mCorrelationWaiter.complete( szCorrelationGuid, frame.getProcessRuntimeMeta() );
                break;
            }
            case ERROR: {
                this.mCorrelationWaiter.completeExceptionally( szCorrelationGuid, new GrpcRemoteProcessControlException( frame.getError().getMessage() ) );
                break;
            }
            default: {
                this.log.info( "[GrpcControlFrameReceived] (ClientId: `{}`, Type: `{}`) <Pass>", session.clientId(), frame.getFrameType() );
                break;
            }
        }
    }

    @Override
    public void registerController( Object controller ) throws RemoteProcessServiceRPCException {
        this.mProcessorLifecycleController = controller;
        this.log.info( "[GrpcControlControllerRegistered] (Controller: `{}`) <Done>", controller.getClass().getName() );
    }

    @Override
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException {
        this.log.info( "[GrpcControlIfaceCompileSkipped] gRPC control uses generated lifecycle iface. <Pass>" );
    }

    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        if ( this.mGrpcAppointServer == null ) {
            throw new RemoteProcessServiceRPCException( "GrpcAppointServer is required." );
        }

        if ( !this.mGrpcAppointServer.isShutdown() ) {
            this.log.info( "[GrpcControlTransportStarted] gRPC appoint server has already started. <Pass>" );
            return;
        }

        try {
            this.mGrpcAppointServer.serverBuilder().addService( new GrpcRemoteProcessControlService( this ) );
            if ( this.mProcessorLifecycleController != null ) {
                this.mGrpcAppointServer.serverBuilder().addService(
                        new GrpcProcessorLifecycleService( this.mProcessorLifecycleController )
                );
            }
            this.mGrpcAppointServer.execute();
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    @Override
    public void terminateService() throws IllegalStateException {
        if ( this.mGrpcAppointServer == null ) {
            throw new IllegalStateException( "Grpc control transport dose not started yet." );
        }

        this.mGrpcAppointServer.shutdown();
    }

    @Override
    public boolean isStarted() {
        return this.mGrpcAppointServer != null && !this.mGrpcAppointServer.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.mGrpcAppointServer == null || this.mGrpcAppointServer.isTerminated();
    }

    @Override
    public void startRemoteUProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        try {
            this.sendAndAwaitCommandResult( clientId, this.mFrameMapper.processIdFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.START_REMOTE_PROCESS, pid ) );
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult(
                    clientId,
                    this.mFrameMapper.commandFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.VITALIZE_REMOTE_PROCESS, processDTO )
            );
            return this.mFrameMapper.toVitalizationResponse( result, processDTO );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse createRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult(
                    clientId,
                    this.mFrameMapper.commandFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.CREATE_REMOTE_PROCESS, processDTO )
            );
            return this.mFrameMapper.toVitalizationResponse( result, processDTO );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public boolean hasOwnProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        return this.queryBooleanCommand( clientId, RemoteProcessControlFrameType.HAS_OWN_PROCESS, pid );
    }

    @Override
    public boolean containProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        return this.queryBooleanCommand( clientId, RemoteProcessControlFrameType.CONTAIN_PROCESS, pid );
    }

    @Override
    public UProcessRuntimeMeta queryProcessRuntimeMeta( long clientId, GUID pid ) throws RemoteProcessLifecycleException {
        try {
            Object result = this.sendAndAwait(
                    clientId,
                    this.mFrameMapper.processIdFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.QUERY_PROCESS_RUNTIME_META, pid )
            );
            return this.mFrameMapper.toRuntimeMeta( (com.walnut.odin.proc.server.transport.grpc.lifecycle.ProcessRuntimeMeta) result );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    protected boolean queryBooleanCommand( long clientId, RemoteProcessControlFrameType type, GUID pid ) throws RemoteProcessServiceRPCException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult( clientId, this.mFrameMapper.processIdFrame( clientId, this.nextGuidString(), type, pid ) );
            return result != null && result.getSuccess();
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected CommandResult sendAndAwaitCommandResult( long clientId, RemoteProcessControlFrame frame ) throws Exception {
        return (CommandResult) this.sendAndAwait( clientId, frame );
    }

    protected Object sendAndAwait( long clientId, RemoteProcessControlFrame frame ) throws Exception {
        GrpcRemoteProcessControlSession session = this.resolveActiveSession( clientId );
        if ( session == null ) {
            throw new GrpcRemoteProcessControlException( "No active gRPC client session: " + clientId );
        }
        this.mCorrelationWaiter.prepare( frame.getCorrelationGuid() );
        session.send( frame );
        return this.mCorrelationWaiter.await( frame.getCorrelationGuid(), 30000L );
    }

    protected GrpcRemoteProcessControlSession resolveActiveSession( long clientId ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( clientId );
        if ( clientile == null ) {
            return null;
        }
        for ( com.walnut.odin.proc.server.transport.RemoteProcessControlSession session : clientile.sessions() ) {
            if ( session instanceof GrpcRemoteProcessControlSession && session.isActive() ) {
                return (GrpcRemoteProcessControlSession) session;
            }
        }
        return null;
    }

}
