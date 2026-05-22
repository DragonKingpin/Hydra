package com.walnut.odin.proc.server.transport.grpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;

public class GrpcRemoteProcessControlTransport implements RemoteProcessControlTransport {

    protected Logger                            log = LoggerFactory.getLogger( this.getClass() );

    protected RemoteProcessManagerServer        mRemoteProcessManagerServer;

    protected GrpcAppointServer                 mGrpcAppointServer;

    protected RemoteProcessControlEventHooker   mEventHooker;

    protected Map<Long, GrpcRemoteProcessControlClientile> mClientileMap;

    protected GrpcCorrelationWaiter<Object>     mCorrelationWaiter;

    public GrpcRemoteProcessControlTransport( RemoteProcessManagerServer remoteProcessManagerServer,
                                              GrpcAppointServer grpcAppointServer,
                                              RemoteProcessControlEventHooker eventHooker ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
        this.mGrpcAppointServer          = grpcAppointServer;
        this.mEventHooker                = eventHooker;
        this.mClientileMap               = new ConcurrentHashMap<>();
        this.mCorrelationWaiter          = new GrpcCorrelationWaiter<>();
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
    public void registerController( Object controller ) throws RemoteProcessServiceRPCException {
        this.log.info( "[GrpcControlControllerRegisterSkipped] gRPC control uses generated service binding. <Pass>" );
    }

    @Override
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException {
        throw new NotImplementedException( "gRPC control does not support runtime iface compile." );
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
        throw new RemoteProcessServiceRPCException( new NotImplementedException( "gRPC lifecycle proto binding is not generated yet." ) );
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        throw new RemoteProcessLifecycleException( new NotImplementedException( "gRPC lifecycle proto binding is not generated yet." ) );
    }

    @Override
    public RemoteVitalizationResponse createRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        throw new RemoteProcessLifecycleException( new NotImplementedException( "gRPC lifecycle proto binding is not generated yet." ) );
    }

    @Override
    public boolean hasOwnProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        throw new RemoteProcessServiceRPCException( new NotImplementedException( "gRPC lifecycle proto binding is not generated yet." ) );
    }

    @Override
    public boolean containProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        throw new RemoteProcessServiceRPCException( new NotImplementedException( "gRPC lifecycle proto binding is not generated yet." ) );
    }

    @Override
    public UProcessRuntimeMeta queryProcessRuntimeMeta( long clientId, GUID pid ) throws RemoteProcessLifecycleException {
        throw new RemoteProcessLifecycleException( new NotImplementedException( "gRPC lifecycle proto binding is not generated yet." ) );
    }

}
