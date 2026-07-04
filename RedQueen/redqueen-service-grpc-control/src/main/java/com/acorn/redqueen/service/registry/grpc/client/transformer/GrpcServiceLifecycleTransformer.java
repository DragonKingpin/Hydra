package com.acorn.redqueen.service.registry.grpc.client.transformer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ClientMuster;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.DeregisterCommand;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.Heartbeat;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.RegisterServiceAccepted;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.RegisterServiceCommand;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrameType;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ShutdownServiceAccepted;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ShutdownServiceCommand;
import com.pinecone.hydra.service.registry.instruction.ServiceShutdownInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceDeregisterInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceRegisterInstruction;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;

public class GrpcServiceLifecycleTransformer implements Pinenut {

    protected GuidAllocator mGuidAllocator;

    public GrpcServiceLifecycleTransformer( GuidAllocator guidAllocator ) {
        this.mGuidAllocator = guidAllocator;
    }

    public ServiceControlFrame clientMusterFrame( long nClientId, String szClientName ) {
        return this.baseFrame( nClientId, "", "", ServiceControlFrameType.CLIENT_MUSTER )
                .setClientMuster(
                        ClientMuster.newBuilder()
                                .setClientId( nClientId )
                                .setClientName( this.safe( szClientName ) )
                                .setTransportVersion( "redqueen-grpc-service-control" )
                                .build()
                )
                .build();
    }

    public ServiceControlFrame heartbeatFrame( long nClientId, String szSessionGuid, GUID instanceGuid ) {
        return this.baseFrame( nClientId, szSessionGuid, "", ServiceControlFrameType.HEARTBEAT )
                .setHeartbeat(
                        Heartbeat.newBuilder()
                                .setInstanceGuid( this.safe( instanceGuid ) )
                                .setStatus( "ACTIVE" )
                                .setClientTimeMillis( System.currentTimeMillis() )
                                .build()
                )
                .build();
    }

    public ServiceControlFrame registerFrame(
            long nClientId,
            String szSessionGuid,
            ServiceRegisterInstruction command
    ) {
        RegisterServiceCommand.Builder builder = RegisterServiceCommand.newBuilder();
        if ( command != null ) {
            builder.setServiceGuid( this.safe( command.getServiceGuid() ) );
            builder.setDeployGuid( this.safe( command.getDeployGuid() ) );
            builder.setInstanceGuid( this.safe( command.getInstanceGuid() ) );
            builder.setEndpointProtocol( this.safe( command.getEndpointProtocol() ) );
            builder.setEndpointHost( this.safe( command.getEndpointHost() ) );
            if ( command.getEndpointPort() != null ) {
                builder.setEndpointPort( command.getEndpointPort() );
            }
            builder.setEndpointPath( this.safe( command.getEndpointPath() ) );
            builder.setEndpointAddress( this.safe( command.getEndpointAddress() ) );
            builder.setVersion( this.safe( command.getVersion() ) );
            builder.setZone( this.safe( command.getZone() ) );
            if ( command.getWeight() != null ) {
                builder.setWeight( command.getWeight() );
            }
            builder.setMetadataJson( this.safe( command.getMetadataJson() ) );
            builder.setRuntimeNodeId( this.safe( command.getRuntimeNodeId() ) );
            builder.setRuntimeNodeAlias( this.safe( command.getRuntimeNodeAlias() ) );
            builder.setRuntimeNodeMetadataJson( this.safe( command.getRuntimeNodeMetadataJson() ) );
        }

        return this.baseFrame( nClientId, szSessionGuid, "", ServiceControlFrameType.REGISTER_SERVICE )
                .setRegisterService( builder.build() )
                .build();
    }

    public ServiceClientRegisterResult toRegisterResult( RegisterServiceAccepted accepted ) {
        ServiceClientRegisterResult result = new ServiceClientRegisterResult();
        if ( accepted == null ) {
            return result;
        }

        result.setInstanceGuid( this.parseGuid( accepted.getInstanceGuid() ) );
        result.setServiceGuid( this.parseGuid( accepted.getServiceGuid() ) );
        result.setStatus( accepted.getStatus() );
        result.setRegisterTimeMillis( accepted.getRegisterTimeMillis() );
        result.setExpireTimeMillis( accepted.getExpireTimeMillis() );
        return result;
    }

    public ServiceControlFrame deregisterFrame(
            long nClientId,
            String szSessionGuid,
            ServiceDeregisterInstruction command
    ) {
        DeregisterCommand.Builder builder = DeregisterCommand.newBuilder();
        if ( command != null ) {
            builder.setInstanceGuid( this.safe( command.getInstanceGuid() ) );
            builder.setReason( this.safe( command.getReason() ) );
        }

        return this.baseFrame( nClientId, szSessionGuid, "", ServiceControlFrameType.DEREGISTER )
                .setDeregister( builder.build() )
                .build();
    }

    public ServiceClientDeregisterResult toDeregisterResult(
            ServiceDeregisterInstruction command,
            String szStatus,
            String szReason
    ) {
        ServiceClientDeregisterResult result = new ServiceClientDeregisterResult();
        if ( command != null ) {
            result.setInstanceGuid( command.getInstanceGuid() );
        }
        result.setSuccess( true );
        result.setReason( szReason == null || szReason.isBlank() ? szStatus : szReason );
        return result;
    }

    public ServiceShutdownInstruction toShutdownInstruction( ShutdownServiceCommand command ) {
        ServiceShutdownInstruction instruction = new ServiceShutdownInstruction();
        if ( command == null ) {
            return instruction;
        }

        instruction.setInstanceGuid( this.parseGuid( command.getInstanceGuid() ) );
        instruction.setReason( command.getReason() );
        return instruction;
    }

    public ServiceControlFrame shutdownAcceptedFrame(
            long nClientId,
            String szSessionGuid,
            ServiceControlFrame request
    ) {
        ShutdownServiceAccepted.Builder builder = ShutdownServiceAccepted.newBuilder();
        if ( request != null && request.hasShutdownService() ) {
            builder.setInstanceGuid( request.getShutdownService().getInstanceGuid() );
            builder.setReason( request.getShutdownService().getReason() );
        }

        return this.baseFrame(
                        nClientId,
                        szSessionGuid,
                        request == null ? "" : request.getFrameGuid(),
                        ServiceControlFrameType.SHUTDOWN_ACCEPTED
                )
                .setShutdownAccepted( builder.build() )
                .build();
    }

    protected ServiceControlFrame.Builder baseFrame(
            long nClientId,
            String szSessionGuid,
            String szCorrelationGuid,
            ServiceControlFrameType frameType
    ) {
        return ServiceControlFrame.newBuilder()
                .setFrameGuid( this.nextGuidString() )
                .setCorrelationGuid( this.safe( szCorrelationGuid ) )
                .setClientId( nClientId )
                .setSessionGuid( this.safe( szSessionGuid ) )
                .setCreateTimeMillis( System.currentTimeMillis() )
                .setFrameType( frameType );
    }

    protected String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    protected GUID parseGuid( String szGuid ) {
        if ( szGuid == null || szGuid.trim().isEmpty() ) {
            return null;
        }
        return this.mGuidAllocator.parse( szGuid );
    }

    protected String safe( GUID guid ) {
        if ( guid == null ) {
            return "";
        }
        return guid.toString();
    }

    protected String safe( String szValue ) {
        if ( szValue == null ) {
            return "";
        }
        return szValue;
    }

}


