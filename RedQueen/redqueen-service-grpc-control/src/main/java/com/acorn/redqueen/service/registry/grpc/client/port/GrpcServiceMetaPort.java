package com.acorn.redqueen.service.registry.grpc.client.port;

import java.util.ArrayList;
import java.util.List;

import com.acorn.redqueen.service.registry.grpc.client.transformer.GrpcServiceMetaTransformer;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaGrpc;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;
import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;

public class GrpcServiceMetaPort implements ServiceMetaPort {

    protected GrpcAppointClient mGrpcAppointClient;

    protected GrpcServiceMetaTransformer mTransformer;

    public GrpcServiceMetaPort( GrpcAppointClient grpcAppointClient ) {
        this.mGrpcAppointClient = grpcAppointClient;
        this.mTransformer = new GrpcServiceMetaTransformer();
    }

    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long nClientId ) {
        ServiceMetaListReply reply = this.stub().fetchServiceInsMetaByClientId(
                FetchServiceInsMetaByClientIdRequest.newBuilder().setClientId( nClientId ).build()
        );
        return this.toServiceMetaDTOList( reply );
    }

    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String szServiceId ) {
        ServiceMetaListReply reply = this.stub().fetchServiceInsMetaByServiceId(
                FetchServiceInsMetaByServiceIdRequest.newBuilder().setServiceId( this.safe( szServiceId ) ).build()
        );
        return this.toServiceMetaDTOList( reply );
    }

    @Override
    public ServiceMetaDTO queryServiceMetaByPath( String szPath ) {
        return this.mTransformer.toServiceMetaDTO(
                this.stub().queryServiceMetaByPath(
                        QueryServiceMetaByPathRequest.newBuilder().setPath( this.safe( szPath ) ).build()
                ).getMeta()
        );
    }

    @Override
    public ServiceMetaDTO queryServiceMetaByGuid( String szGuid ) {
        return this.mTransformer.toServiceMetaDTO(
                this.stub().queryServiceMetaByGuid(
                        QueryServiceMetaByGuidRequest.newBuilder().setGuid( this.safe( szGuid ) ).build()
                ).getMeta()
        );
    }

    @Override
    public String evalCreationStatement( String szJonsStatement ) {
        return this.stub().evalCreationStatement(
                EvalCreationStatementRequest.newBuilder().setJonsStatement( this.safe( szJonsStatement ) ).build()
        ).getGuid();
    }

    @Override
    public String createNewService( String szParentAppPath, ServiceMetaDTO meta ) {
        return this.stub().createNewService(
                CreateNewServiceRequest.newBuilder()
                        .setParentAppPath( this.safe( szParentAppPath ) )
                        .setMeta( this.mTransformer.toProtocolMeta( meta ) )
                        .build()
        ).getGuid();
    }

    protected ServiceMetaGrpc.ServiceMetaBlockingStub stub() {
        try {
            if ( this.mGrpcAppointClient.isShutdown() ) {
                this.mGrpcAppointClient.execute();
            }
        }
        catch ( Exception e ) {
            throw new IllegalStateException( "gRPC service meta client cannot open channel.", e );
        }
        return ServiceMetaGrpc.newBlockingStub( this.mGrpcAppointClient.getChannel() );
    }

    protected List<ServiceMetaDTO> toServiceMetaDTOList( ServiceMetaListReply reply ) {
        List<ServiceMetaDTO> items = new ArrayList<>();
        for ( com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO item : reply.getItemsList() ) {
            ServiceMetaDTO dto = this.mTransformer.toServiceMetaDTO( item );
            if ( dto != null ) {
                items.add( dto );
            }
        }
        return items;
    }

    protected String safe( String value ) {
        if ( value == null ) {
            return "";
        }
        return value;
    }

}
