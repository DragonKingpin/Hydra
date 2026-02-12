package com.pinecone.hydra.service.registry.grpc.server;

import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.grpc.server.meta.ClientIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.meta.CreateNewServiceRequest;
import com.pinecone.hydra.service.registry.grpc.server.meta.EvalRequest;
import com.pinecone.hydra.service.registry.grpc.server.meta.GuidRequest;
import com.pinecone.hydra.service.registry.grpc.server.meta.PathRequest;
import com.pinecone.hydra.service.registry.grpc.server.meta.ServiceIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOListReply;
import com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTOReply;
import com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaGrpc;
import com.pinecone.hydra.service.registry.grpc.server.meta.StringReply;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.ServiceMetaService;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class GrpcServiceMetaService extends ServiceMetaGrpc.ServiceMetaImplBase {

    private final ServiceMetaService serviceMetaService;

    public GrpcServiceMetaService(ServiceManager serviceManager) {
        this.serviceMetaService = serviceManager.getServiceMetaService();
    }

    @Override
    public void fetchServiceInsMetaByClientId(ClientIdRequest request, StreamObserver<ServiceMetaDTOListReply> responseObserver) {

        List<ServiceMetaDTO> list = this.serviceMetaService.fetchServiceInsMetaByClientId(request.getClientId());

        ServiceMetaDTOListReply.Builder builder = ServiceMetaDTOListReply.newBuilder();

        if (list != null) {
            for (ServiceMetaDTO dto : list) {
                builder.addMetas(this.toProto(dto));
            }
        }

        ServiceMetaDTOListReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void fetchServiceInsMetaByServiceId(ServiceIdRequest request, StreamObserver<ServiceMetaDTOListReply> responseObserver) {
        List<ServiceMetaDTO> list = this.serviceMetaService.fetchServiceInsMetaByServiceId(request.getServiceId());

        ServiceMetaDTOListReply.Builder builder = ServiceMetaDTOListReply.newBuilder();

        if (list != null) {
            for (ServiceMetaDTO dto : list) {
                builder.addMetas(this.toProto(dto));
            }
        }

        ServiceMetaDTOListReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void queryServiceMetaByPath(PathRequest request, StreamObserver<ServiceMetaDTOReply> responseObserver) {
        ServiceMetaDTO dto = this.serviceMetaService.queryServiceMetaByPath(request.getPath()
        );

        ServiceMetaDTOReply.Builder builder = ServiceMetaDTOReply.newBuilder();

        if (dto != null) {
            builder.setMeta(this.toProto(dto));
        }

        ServiceMetaDTOReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void queryServiceMetaByGuid(GuidRequest request, StreamObserver<ServiceMetaDTOReply> responseObserver) {
        ServiceMetaDTO dto = this.serviceMetaService.queryServiceMetaByGuid(request.getGuid());

        ServiceMetaDTOReply.Builder builder = ServiceMetaDTOReply.newBuilder();

        if (dto != null) {
            builder.setMeta(this.toProto(dto));
        }

        ServiceMetaDTOReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void evalCreationStatement(EvalRequest request, StreamObserver<StringReply> responseObserver) {
        String result = this.serviceMetaService.evalCreationStatement(request.getJsonStatement());

        StringReply.Builder builder = StringReply.newBuilder();

        if (result != null) {
            builder.setValue(result);
        }

        StringReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void createNewService(CreateNewServiceRequest request, StreamObserver<StringReply> responseObserver) {
        String result = this.serviceMetaService.createNewService(request.getParentAppPath(), this.fromProto(request.getMeta()));

        StringReply.Builder builder = StringReply.newBuilder();

        if (result != null) {
            builder.setValue(result);
        }

        StringReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    private com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO toProto(ServiceMetaDTO dto) {
        com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO.Builder builder =
                com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO.newBuilder();

        if (dto.getGuid() != null) {
            builder.setGuid(dto.getGuid());
        }
        if (dto.getName() != null) {
            builder.setName(dto.getName());
        }
        if (dto.getType() != null) {
            builder.setType(dto.getType());
        }
        if (dto.getDisplayName() != null) {
            builder.setDisplayName(dto.getDisplayName());
        }
        if (dto.getDescription() != null) {
            builder.setDescription(dto.getDescription());
        }
        if (dto.getFullName() != null) {
            builder.setFullName(dto.getFullName());
        }
        if (dto.getGroupNamespace() != null) {
            builder.setGroupNamespace(dto.getGroupNamespace());
        }
        if (dto.getGroupName() != null) {
            builder.setGroupName(dto.getGroupName());
        }
        if (dto.getScenario() != null) {
            builder.setScenario(dto.getScenario());
        }
        if (dto.getPrimaryImplLang() != null) {
            builder.setPrimaryImplLang(dto.getPrimaryImplLang());
        }
        if (dto.getExtraInformation() != null) {
            builder.setExtraInformation(dto.getExtraInformation());
        }
        if (dto.getLevel() != null) {
            builder.setLevel(dto.getLevel());
        }

        return builder.build();
    }

    private ServiceMetaDTO fromProto(com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO proto) {
        ServiceMetaDTO dto = new ServiceMetaDTO();

        dto.setGuid(proto.getGuid());
        dto.setName(proto.getName());
        dto.setType(proto.getType());
        dto.setDisplayName(proto.getDisplayName());
        dto.setDescription(proto.getDescription());
        dto.setFullName(proto.getFullName());
        dto.setGroupNamespace(proto.getGroupNamespace());
        dto.setGroupName(proto.getGroupName());
        dto.setScenario(proto.getScenario());
        dto.setPrimaryImplLang(proto.getPrimaryImplLang());
        dto.setExtraInformation(proto.getExtraInformation());
        dto.setLevel(proto.getLevel());

        return dto;
    }
}
