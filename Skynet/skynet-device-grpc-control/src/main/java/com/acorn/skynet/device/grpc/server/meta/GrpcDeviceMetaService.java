package com.acorn.skynet.device.grpc.server.meta;

import com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaGrpc;
import com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaReply;
import com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaUpdateReply;
import com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByGuidRequest;
import com.acorn.skynet.device.grpc.protocol.meta.proto.QueryDeviceMetaByPathRequest;
import com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByGuidRequest;
import com.acorn.skynet.device.grpc.protocol.meta.proto.UpdateDeviceMetaByPathRequest;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.server.DeviceManager;

import io.grpc.stub.StreamObserver;

public class GrpcDeviceMetaService extends DeviceMetaGrpc.DeviceMetaImplBase implements Pinenut {

    protected final DeviceManager deviceManager;

    protected final GrpcDeviceMetaTransformer transformer;

    public GrpcDeviceMetaService( DeviceManager deviceManager ) {
        this.deviceManager = deviceManager;
        this.transformer = new GrpcDeviceMetaTransformer();
    }

    @Override
    public void queryDeviceMetaByPath(
            QueryDeviceMetaByPathRequest request,
            StreamObserver<DeviceMetaReply> responseObserver
    ) {
        DeviceMetaDTO meta = this.deviceManager.deviceMetaService().queryDeviceMetaByPath( request.getPath() );
        responseObserver.onNext(
                DeviceMetaReply.newBuilder().setMeta( this.transformer.toProtocolMeta( meta ) ).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void queryDeviceMetaByGuid(
            QueryDeviceMetaByGuidRequest request,
            StreamObserver<DeviceMetaReply> responseObserver
    ) {
        DeviceMetaDTO meta = this.deviceManager.deviceMetaService().queryDeviceMetaByGuid( request.getGuid() );
        responseObserver.onNext(
                DeviceMetaReply.newBuilder().setMeta( this.transformer.toProtocolMeta( meta ) ).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateDeviceMetaByPath(
            UpdateDeviceMetaByPathRequest request,
            StreamObserver<DeviceMetaUpdateReply> responseObserver
    ) {
        boolean updated = this.deviceManager.deviceMetaService().updateDeviceMetaByPath(
                request.getPath(),
                this.transformer.toDeviceMetaDTO( request.getMeta() )
        );
        responseObserver.onNext( DeviceMetaUpdateReply.newBuilder().setUpdated( updated ).build() );
        responseObserver.onCompleted();
    }

    @Override
    public void updateDeviceMetaByGuid(
            UpdateDeviceMetaByGuidRequest request,
            StreamObserver<DeviceMetaUpdateReply> responseObserver
    ) {
        boolean updated = this.deviceManager.deviceMetaService().updateDeviceMetaByGuid(
                request.getGuid(),
                this.transformer.toDeviceMetaDTO( request.getMeta() )
        );
        responseObserver.onNext( DeviceMetaUpdateReply.newBuilder().setUpdated( updated ).build() );
        responseObserver.onCompleted();
    }
}
