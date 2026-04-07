package com.pinecone.hydra.service.registry.grpc.server;

import java.net.SocketAddress;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage;
import com.pinecone.hydra.service.registry.grpc.server.cs.ControlStreamGrpc;
import com.pinecone.hydra.service.registry.server.ServiceManager;

import io.grpc.Context;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class GrpcControlStreamService extends ControlStreamGrpc.ControlStreamImplBase {

    private final ServiceManager           serviceManager;
    private final GrpcServiceAppointServer appointServer;
    private final GuidAllocator            guidAllocator;

    public GrpcControlStreamService( ServiceManager serviceManager, GrpcServiceAppointServer appointServer ) {
        this.serviceManager = serviceManager;
        this.appointServer  = appointServer;
        this.guidAllocator  = serviceManager.getServicesInstrument().getGuidAllocator();
    }

    @Override
    public StreamObserver<ControlMessage> connect( StreamObserver<ControlMessage> responseObserver ) {
        final SocketAddress remoteAddr = ClientAddress.CLIENT_ADDR.get();
        final String connectId = remoteAddr.toString() + "_" + this.guidAllocator.nextGUID().toString();

        return new StreamObserver<>() {
            Long clientId = null;

            @Override
            public void onNext( ControlMessage message ) {
                if ( this.clientId == null ) {
                    this.clientId = message.getClientId();

                    GrpcSession session = new GrpcSession( connectId, remoteAddr, responseObserver );

                    serviceManager.serviceEventHooker().afterNewConnectionInbound(
                            this.clientId,
                            connectId,
                            session,
                            null,
                            () -> new GrpcServiceClientile(appointServer)
                    );
                }

                // 这里可以处理心跳或其他控制指令
            }

            @Override
            public void onError( Throwable t ) {
                if ( t instanceof StatusRuntimeException ) {
                    serviceManager.getLogger().info(
                            "[ServiceLifecycle] `{}` has requested `cancelled` to detach, with what '{}', addr: `{}`.",
                            this.clientId, t.getMessage(),  remoteAddr.toString()
                    );
                }
                else {
                    serviceManager.getLogger().error(
                            "[ServiceFatality] `{}` has provoked `exception` to detach, with what '{}', addr: `{}`.",
                            this.clientId, t.getMessage(),  remoteAddr.toString()
                    );
                }
                this.detach();
            }

            @Override
            public void onCompleted() {
                this.detach();
            }

            private void detach() {
                if ( this.clientId != null ) {
                    serviceManager.serviceEventHooker().afterConnectionDetach( this.clientId, connectId, null );
                }
            }
        };
    }
}