package com.acorn.skynet.device.grpc.transformer;

import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.ClientMuster;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.ClientReady;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeregisterDeviceCommand;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.Deregistered;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrameType;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.ErrorFrame;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.RegisterDeviceAccepted;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.RegisterDeviceCommand;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.ShutdownDeviceAccepted;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.ShutdownDeviceCommand;
import com.acorn.skynet.device.grpc.server.GrpcDeviceLifecycleSession;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceShutdownInstruction;

public class GrpcDeviceLifecycleTransformer implements Pinenut {

    protected final GuidAllocator guidAllocator;

    public GrpcDeviceLifecycleTransformer( GuidAllocator guidAllocator ) {
        this.guidAllocator = guidAllocator;
    }

    public DeviceLifecycleFrame clientMusterFrame( long clientId, String clientName ) {
        return this.baseFrame( clientId, null, DeviceLifecycleFrameType.CLIENT_MUSTER )
                .setClientMuster( ClientMuster.newBuilder()
                        .setClientId( clientId )
                        .setClientName( this.safe( clientName ) )
                        .setTransportVersion( "1.0.0" )
                        .build() )
                .build();
    }

    public DeviceLifecycleFrame clientReadyFrame( DeviceLifecycleFrame request, GrpcDeviceLifecycleSession session ) {
        return this.responseFrame( request, session.clientId(), session.sessionGuid(), DeviceLifecycleFrameType.CLIENT_READY )
                .setClientReady( ClientReady.newBuilder()
                        .setClientId( session.clientId() )
                        .setSessionGuid( session.sessionGuid() )
                        .setRemoteAddress( this.safe( session.remoteAddress() ) )
                        .setServerTimeMillis( System.currentTimeMillis() )
                        .build() )
                .build();
    }

    public DeviceLifecycleFrame registerFrame( long clientId, String sessionGuid, DeviceRegisterInstruction instruction ) {
        RegisterDeviceCommand.Builder command = RegisterDeviceCommand.newBuilder();
        if ( instruction != null ) {
            command.setDeviceGuid( this.toString( instruction.getDeviceGuid() ) );
            command.setDeviceGuidText( this.safe( instruction.getDeviceGuidText() ) );
            command.setDevicePath( this.safe( instruction.getDevicePath() ) );
            if ( instruction.getClientId() != null ) {
                command.setClientId( instruction.getClientId() );
            }
            command.setInstanceGuid( this.toString( instruction.getInstanceGuid() ) );
            command.setEndpointProtocol( this.safe( instruction.getEndpointProtocol() ) );
            command.setEndpointHost( this.safe( instruction.getEndpointHost() ) );
            if ( instruction.getEndpointPort() != null ) {
                command.setEndpointPort( instruction.getEndpointPort() );
            }
            command.setEndpointPath( this.safe( instruction.getEndpointPath() ) );
            command.setEndpointAddress( this.safe( instruction.getEndpointAddress() ) );
            command.setMetadataJson( this.safe( instruction.getMetadataJson() ) );
        }

        return this.baseFrame( clientId, sessionGuid, DeviceLifecycleFrameType.REGISTER_DEVICE )
                .setRegisterDevice( command.build() )
                .build();
    }

    public DeviceRegisterInstruction toRegisterInstruction( RegisterDeviceCommand command ) {
        DeviceRegisterInstruction instruction = new DeviceRegisterInstruction();
        if ( command == null ) {
            return instruction;
        }

        instruction.setDeviceGuid( this.toGuid( command.getDeviceGuid() ) );
        instruction.setDeviceGuidText( command.getDeviceGuidText() );
        instruction.setDevicePath( command.getDevicePath() );
        if ( command.getClientId() > 0 ) {
            instruction.setClientId( command.getClientId() );
        }
        instruction.setInstanceGuid( this.toGuid( command.getInstanceGuid() ) );
        instruction.setEndpointProtocol( command.getEndpointProtocol() );
        instruction.setEndpointHost( command.getEndpointHost() );
        if ( command.getEndpointPort() > 0 ) {
            instruction.setEndpointPort( command.getEndpointPort() );
        }
        instruction.setEndpointPath( command.getEndpointPath() );
        instruction.setEndpointAddress( command.getEndpointAddress() );
        instruction.setMetadataJson( command.getMetadataJson() );
        return instruction;
    }

    public DeviceLifecycleFrame registerAcceptedFrame(
            DeviceLifecycleFrame request,
            GrpcDeviceLifecycleSession session,
            DeviceInstanceEntry instance
    ) {
        RegisterDeviceAccepted.Builder accepted = RegisterDeviceAccepted.newBuilder()
                .setSessionGuid( session.sessionGuid() )
                .setStatus( instance == null || instance.getStatus() == null ? "" : instance.getStatus().getName() );
        if ( instance != null ) {
            accepted.setDeviceGuid( this.toString( instance.getDeviceGuid() ) );
            accepted.setInstanceGuid( this.toString( instance.getInstanceGuid() ) );
            accepted.setClientId( instance.getClientId() );
        }

        return this.responseFrame( request, session.clientId(), session.sessionGuid(), DeviceLifecycleFrameType.REGISTER_ACCEPTED )
                .setRegisterAccepted( accepted.build() )
                .build();
    }

    public DeviceClientRegisterResult toRegisterResult( RegisterDeviceAccepted accepted ) {
        DeviceClientRegisterResult result = new DeviceClientRegisterResult();
        if ( accepted == null ) {
            return result;
        }

        result.setDeviceGuid( this.toGuid( accepted.getDeviceGuid() ) );
        result.setInstanceGuid( this.toGuid( accepted.getInstanceGuid() ) );
        result.setClientId( accepted.getClientId() );
        return result;
    }

    public DeviceLifecycleFrame deregisterFrame( long clientId, String sessionGuid, DeviceDeregisterInstruction instruction ) {
        DeregisterDeviceCommand.Builder command = DeregisterDeviceCommand.newBuilder();
        if ( instruction != null ) {
            command.setInstanceGuid( this.toString( instruction.getInstanceGuid() ) );
            command.setReason( this.safe( instruction.getReason() ) );
        }

        return this.baseFrame( clientId, sessionGuid, DeviceLifecycleFrameType.DEREGISTER_DEVICE )
                .setDeregisterDevice( command.build() )
                .build();
    }

    public DeviceDeregisterInstruction toDeregisterInstruction( DeregisterDeviceCommand command ) {
        DeviceDeregisterInstruction instruction = new DeviceDeregisterInstruction();
        if ( command == null ) {
            return instruction;
        }

        instruction.setInstanceGuid( this.toGuid( command.getInstanceGuid() ) );
        instruction.setReason( command.getReason() );
        return instruction;
    }

    public DeviceLifecycleFrame deregisteredFrame(
            DeviceLifecycleFrame request,
            GrpcDeviceLifecycleSession session,
            String reason
    ) {
        String instanceGuid = request != null && request.hasDeregisterDevice()
                ? request.getDeregisterDevice().getInstanceGuid()
                : "";

        return this.responseFrame( request, session.clientId(), session.sessionGuid(), DeviceLifecycleFrameType.DEREGISTERED )
                .setDeregistered( Deregistered.newBuilder()
                        .setInstanceGuid( this.safe( instanceGuid ) )
                        .setStatus( "Deregistered" )
                        .setReason( this.safe( reason ) )
                        .build() )
                .build();
    }

    public DeviceClientDeregisterResult toDeregisterResult( Deregistered deregistered ) {
        DeviceClientDeregisterResult result = new DeviceClientDeregisterResult();
        if ( deregistered != null ) {
            result.setInstanceGuid( this.toGuid( deregistered.getInstanceGuid() ) );
        }
        return result;
    }

    public DeviceLifecycleFrame shutdownDeviceFrame( GrpcDeviceLifecycleSession session, GUID instanceGuid, String reason ) {
        return this.baseFrame( session.clientId(), session.sessionGuid(), DeviceLifecycleFrameType.SHUTDOWN_DEVICE )
                .setShutdownDevice( ShutdownDeviceCommand.newBuilder()
                        .setInstanceGuid( this.toString( instanceGuid ) )
                        .setReason( this.safe( reason ) )
                        .build() )
                .build();
    }

    public DeviceShutdownInstruction toShutdownInstruction( ShutdownDeviceCommand command ) {
        DeviceShutdownInstruction instruction = new DeviceShutdownInstruction();
        if ( command != null ) {
            instruction.setInstanceGuid( this.toGuid( command.getInstanceGuid() ) );
            instruction.setReason( command.getReason() );
        }
        return instruction;
    }

    public DeviceLifecycleFrame shutdownAcceptedFrame( long clientId, String sessionGuid, DeviceLifecycleFrame request ) {
        ShutdownDeviceAccepted.Builder accepted = ShutdownDeviceAccepted.newBuilder();
        if ( request != null && request.hasShutdownDevice() ) {
            accepted.setInstanceGuid( request.getShutdownDevice().getInstanceGuid() );
            accepted.setReason( request.getShutdownDevice().getReason() );
        }
        return this.responseFrame( request, clientId, sessionGuid, DeviceLifecycleFrameType.SHUTDOWN_ACCEPTED )
                .setShutdownAccepted( accepted.build() )
                .build();
    }

    public DeviceLifecycleFrame errorFrame(
            DeviceLifecycleFrame request,
            long clientId,
            String sessionGuid,
            String code,
            String message
    ) {
        return this.responseFrame( request, clientId, sessionGuid, DeviceLifecycleFrameType.ERROR )
                .setError( ErrorFrame.newBuilder()
                        .setCode( this.safe( code ) )
                        .setMessage( this.safe( message ) )
                        .build() )
                .build();
    }

    protected DeviceLifecycleFrame.Builder baseFrame(
            long clientId,
            String sessionGuid,
            DeviceLifecycleFrameType frameType
    ) {
        return DeviceLifecycleFrame.newBuilder()
                .setFrameGuid( this.guidAllocator.nextGUID().toString() )
                .setClientId( clientId )
                .setSessionGuid( this.safe( sessionGuid ) )
                .setCreateTimeMillis( System.currentTimeMillis() )
                .setFrameType( frameType );
    }

    protected DeviceLifecycleFrame.Builder responseFrame(
            DeviceLifecycleFrame request,
            long clientId,
            String sessionGuid,
            DeviceLifecycleFrameType frameType
    ) {
        DeviceLifecycleFrame.Builder builder = this.baseFrame( clientId, sessionGuid, frameType );
        if ( request != null ) {
            builder.setCorrelationGuid( request.getFrameGuid() );
        }
        return builder;
    }

    protected GUID toGuid( String guid ) {
        if ( guid == null || guid.trim().isEmpty() ) {
            return null;
        }
        return this.guidAllocator.parse( guid );
    }

    protected String toString( GUID guid ) {
        return guid == null ? "" : guid.toString();
    }

    protected String safe( String value ) {
        return value == null ? "" : value;
    }
}
