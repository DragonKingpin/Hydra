package com.pinecone.hydra.service.registry.grpc.server.iface;

import java.util.ArrayList;
import java.util.List;

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
import com.pinecone.hydra.service.registry.server.ServiceMetaManipulationIface;

public class ServiceMetaManipulationIfaceImpl implements ServiceMetaManipulationIface {

    protected final ServiceMetaGrpc.ServiceMetaBlockingStub metaBlockingStub;

    public ServiceMetaManipulationIfaceImpl( ServiceMetaGrpc.ServiceMetaBlockingStub metaBlockingStub ) {
        this.metaBlockingStub = metaBlockingStub;
    }


    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long clientId ) {
        ClientIdRequest request = ClientIdRequest.newBuilder()
                        .setClientId( clientId )
                        .build();

        ServiceMetaDTOListReply reply = this.metaBlockingStub.fetchServiceInsMetaByClientId( request );

        List<ServiceMetaDTO> result = new ArrayList<>();

        for( com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO proto : reply.getMetasList() ) {
            result.add( this.fromProto( proto ) );
        }

        return result;
    }


    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String serviceId ) {
        ServiceIdRequest request = ServiceIdRequest.newBuilder()
                        .setServiceId( serviceId )
                        .build();

        ServiceMetaDTOListReply reply = this.metaBlockingStub.fetchServiceInsMetaByServiceId( request );

        List<ServiceMetaDTO> result = new ArrayList<>();

        for( com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO proto : reply.getMetasList() ) {
            result.add( this.fromProto( proto ) );
        }
        return result;
    }


    @Override
    public ServiceMetaDTO queryServiceMetaByPath( String path ) {
        PathRequest request = PathRequest.newBuilder()
                        .setPath( path )
                        .build();

        ServiceMetaDTOReply reply = this.metaBlockingStub.queryServiceMetaByPath( request );
        if( reply.hasMeta() ) {
            return this.fromProto( reply.getMeta() );
        }

        return null;
    }


    @Override
    public ServiceMetaDTO queryServiceMetaByGuid( String guid ) {
        GuidRequest request = GuidRequest.newBuilder()
                        .setGuid( guid )
                        .build();

        ServiceMetaDTOReply reply = this.metaBlockingStub.queryServiceMetaByGuid( request );
        if( reply.hasMeta() ) {
            return this.fromProto( reply.getMeta() );
        }
        return null;
    }


    @Override
    public String evalCreationStatement( String jsonStatement ) {
        EvalRequest request = EvalRequest.newBuilder()
                        .setJsonStatement( jsonStatement )
                        .build();
        StringReply reply = this.metaBlockingStub.evalCreationStatement( request );
        return reply.getValue();
    }


    @Override
    public String createNewService( String parentAppPath, ServiceMetaDTO meta ) {
        CreateNewServiceRequest request = CreateNewServiceRequest.newBuilder()
                        .setParentAppPath( parentAppPath )
                        .setMeta( this.toProto( meta ) )
                        .build();
        StringReply reply = this.metaBlockingStub.createNewService( request );
        return reply.getValue();
    }



    protected ServiceMetaDTO fromProto( com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO proto ) {
        ServiceMetaDTO dto = new ServiceMetaDTO();

        dto.setGuid( proto.getGuid() );
        dto.setName( proto.getName() );
        dto.setType( proto.getType() );
        dto.setDisplayName( proto.getDisplayName() );
        dto.setDescription( proto.getDescription() );
        dto.setFullName( proto.getFullName() );
        dto.setGroupNamespace( proto.getGroupNamespace() );
        dto.setGroupName( proto.getGroupName() );
        dto.setScenario( proto.getScenario() );
        dto.setPrimaryImplLang( proto.getPrimaryImplLang() );
        dto.setExtraInformation( proto.getExtraInformation() );
        dto.setLevel( proto.getLevel() );

        return dto;
    }

    protected com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO toProto( ServiceMetaDTO dto ) {
        com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO.Builder builder =
                com.pinecone.hydra.service.registry.grpc.server.meta.ServiceMetaDTO.newBuilder();

        if( dto.getGuid() != null ) {
            builder.setGuid( dto.getGuid() );
        }

        if( dto.getName() != null ) {
            builder.setName( dto.getName() );
        }

        if( dto.getType() != null ) {
            builder.setType( dto.getType() );
        }

        if( dto.getDisplayName() != null ) {
            builder.setDisplayName( dto.getDisplayName() );
        }

        if( dto.getDescription() != null ) {
            builder.setDescription( dto.getDescription() );
        }

        if( dto.getFullName() != null ) {
            builder.setFullName( dto.getFullName() );
        }

        if( dto.getGroupNamespace() != null ) {
            builder.setGroupNamespace( dto.getGroupNamespace() );
        }

        if( dto.getGroupName() != null ) {
            builder.setGroupName( dto.getGroupName() );
        }

        if( dto.getScenario() != null ) {
            builder.setScenario( dto.getScenario() );
        }

        if( dto.getPrimaryImplLang() != null ) {
            builder.setPrimaryImplLang( dto.getPrimaryImplLang() );
        }

        if( dto.getExtraInformation() != null ) {
            builder.setExtraInformation( dto.getExtraInformation() );
        }

        if( dto.getLevel() != null ) {
            builder.setLevel( dto.getLevel() );
        }

        return builder.build();
    }
}
