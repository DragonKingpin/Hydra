package com.walnut.odin.proc.server.transport.grpc;

import java.lang.reflect.Method;

import com.pinecone.framework.system.prototype.Pinenut;

import io.grpc.stub.StreamObserver;

public class GrpcProcessorLifecycleService extends com.walnut.odin.proc.server.transport.grpc.legionary.ProcessorLifecycleGrpc.ProcessorLifecycleImplBase implements Pinenut {

    protected Object mProcessorLifecycleController;

    protected Method mJoinRegimentMethod;

    public GrpcProcessorLifecycleService( Object processorLifecycleController ) {
        this.mProcessorLifecycleController = processorLifecycleController;
        this.mJoinRegimentMethod = this.resolveJoinRegimentMethod( processorLifecycleController );
    }

    @Override
    public void joinRegiment(
            com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest request,
            StreamObserver<com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse> responseObserver
    ) {
        try {
            com.walnut.odin.conduct.entity.RegimentJoinResponse response = this.invokeJoinRegiment(
                    this.toConductRequest( request )
            );
            responseObserver.onNext( this.toGrpcResponse( response ) );
            responseObserver.onCompleted();
        }
        catch ( Exception e ) {
            responseObserver.onNext(
                    com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse.newBuilder()
                            .setErrorMsg( e.getMessage() == null ? e.getClass().getName() : e.getMessage() )
                            .build()
            );
            responseObserver.onCompleted();
        }
    }

    protected Method resolveJoinRegimentMethod( Object controller ) {
        if ( controller == null ) {
            return null;
        }

        try {
            return controller.getClass().getMethod(
                    "joinRegiment",
                    com.walnut.odin.conduct.entity.RegimentJoinRequest.class
            );
        }
        catch ( NoSuchMethodException e ) {
            return null;
        }
    }

    protected com.walnut.odin.conduct.entity.RegimentJoinResponse invokeJoinRegiment(
            com.walnut.odin.conduct.entity.RegimentJoinRequest request
    ) throws Exception {
        if ( this.mProcessorLifecycleController == null || this.mJoinRegimentMethod == null ) {
            com.walnut.odin.conduct.entity.RegimentJoinResponse response = new com.walnut.odin.conduct.entity.RegimentJoinResponse();
            response.setErrorMsg( "ProcessorLifecycleController is not registered." );
            return response;
        }

        Object rawResponse = this.mJoinRegimentMethod.invoke( this.mProcessorLifecycleController, request );
        if ( rawResponse instanceof com.walnut.odin.conduct.entity.RegimentJoinResponse ) {
            return (com.walnut.odin.conduct.entity.RegimentJoinResponse) rawResponse;
        }

        com.walnut.odin.conduct.entity.RegimentJoinResponse response = new com.walnut.odin.conduct.entity.RegimentJoinResponse();
        response.setErrorMsg( "ProcessorLifecycleController.joinRegiment returned invalid response." );
        return response;
    }

    protected com.walnut.odin.conduct.entity.RegimentJoinRequest toConductRequest(
            com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinRequest request
    ) {
        com.walnut.odin.conduct.entity.RegimentJoinRequest conductRequest = new com.walnut.odin.conduct.entity.RegimentJoinRequest();
        conductRequest.setNodeName( request.getNodeName() );
        conductRequest.setClientId( request.getClientId() );
        return conductRequest;
    }

    protected com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse toGrpcResponse(
            com.walnut.odin.conduct.entity.RegimentJoinResponse response
    ) {
        if ( response == null ) {
            return com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse.newBuilder()
                    .setErrorMsg( "ProcessorLifecycleController.joinRegiment returned null." )
                    .build();
        }

        return com.walnut.odin.proc.server.transport.grpc.legionary.RegimentJoinResponse.newBuilder()
                .setGuid( valueOf( response.getGuid() ) )
                .setName( valueOf( response.getName() ) )
                .setClusterPath( valueOf( response.getClusterPath() ) )
                .setClusterName( valueOf( response.getClusterName() ) )
                .setControlClientId( response.getControlClientId() )
                .setPriority( response.getPriority() )
                .setQueueName( valueOf( response.getQueueName() ) )
                .setQueueMaxCapacity( response.getQueueMaxCapacity() )
                .setQueueMinCapacity( response.getQueueMinCapacity() )
                .setQueueRuntimeInstanceCapacity( response.getQueueRuntimeInstanceCapacity() )
                .setErrorMsg( valueOf( response.getErrorMsg() ) )
                .build();
    }

    protected String valueOf( String value ) {
        return value == null ? "" : value;
    }
}
