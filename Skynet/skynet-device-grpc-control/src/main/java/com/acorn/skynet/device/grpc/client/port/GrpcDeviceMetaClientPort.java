package com.acorn.skynet.device.grpc.client.port;

import com.acorn.skynet.device.grpc.client.transformer.GrpcDeviceMetaTransformer;
import com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaGrpc;
import com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest;
import com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest;
import com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest;
import com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest;
import com.pinecone.hydra.device.registry.client.port.DeviceMetaPort;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;

public class GrpcDeviceMetaClientPort implements DeviceMetaPort {

    protected final GrpcAppointClient grpcAppointClient;

    protected final GrpcDeviceMetaTransformer transformer;

    public GrpcDeviceMetaClientPort( GrpcAppointClient grpcAppointClient ) {
        this.grpcAppointClient = grpcAppointClient;
        this.transformer = new GrpcDeviceMetaTransformer();
    }

    @Override
    public DeviceMetaDTO queryDeviceMetaByPath( String path ) {
        return this.transformer.toDeviceMetaDTO(
                this.stub().queryDeviceMetaByPath(
                        QueryDeviceMetaByPathRequest.newBuilder().setPath( this.safe( path ) ).build()
                ).getMeta()
        );
    }

    @Override
    public DeviceMetaDTO queryDeviceMetaByGuid( String guid ) {
        return this.transformer.toDeviceMetaDTO(
                this.stub().queryDeviceMetaByGuid(
                        QueryDeviceMetaByGuidRequest.newBuilder().setGuid( this.safe( guid ) ).build()
                ).getMeta()
        );
    }

    @Override
    public boolean updateDeviceMetaByPath( String path, DeviceMetaDTO meta ) {
        return this.stub().updateDeviceMetaByPath(
                UpdateDeviceMetaByPathRequest.newBuilder()
                        .setPath( this.safe( path ) )
                        .setMeta( this.transformer.toProtocolMeta( meta ) )
                        .build()
        ).getUpdated();
    }

    @Override
    public boolean updateDeviceMetaByGuid( String guid, DeviceMetaDTO meta ) {
        return this.stub().updateDeviceMetaByGuid(
                UpdateDeviceMetaByGuidRequest.newBuilder()
                        .setGuid( this.safe( guid ) )
                        .setMeta( this.transformer.toProtocolMeta( meta ) )
                        .build()
        ).getUpdated();
    }

    protected DeviceMetaGrpc.DeviceMetaBlockingStub stub() {
        try {
            if ( this.grpcAppointClient.isShutdown() ) {
                this.grpcAppointClient.execute();
            }
        }
        catch ( Exception e ) {
            throw new IllegalStateException( "gRPC device meta client cannot open channel.", e );
        }
        return DeviceMetaGrpc.newBlockingStub( this.grpcAppointClient.getChannel() );
    }

    protected String safe( String value ) {
        return value == null ? "" : value;
    }
}
