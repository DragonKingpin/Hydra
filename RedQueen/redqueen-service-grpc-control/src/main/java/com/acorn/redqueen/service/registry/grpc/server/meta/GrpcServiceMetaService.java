package com.acorn.redqueen.service.registry.grpc.server.meta;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.kom.ServiceElementPage;
import com.pinecone.hydra.service.kom.ServiceElementQuery;
import com.pinecone.hydra.service.kom.ServiceInstancePage;
import com.pinecone.hydra.service.kom.ServiceInstanceQuery;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.CreateNewServiceRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.EvalCreationStatementRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchNamespaceChildrenRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByClientIdRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInsMetaByServiceIdRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServiceInstancePageRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.FetchServicePageRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.GuidReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.NamespaceChildrenReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceInstanceRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByGuidRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceMetaByPathRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.QueryServiceRequest;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstancePageReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaGrpc;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaListReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServicePageReply;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceReply;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import io.grpc.stub.StreamObserver;

public class GrpcServiceMetaService extends ServiceMetaGrpc.ServiceMetaImplBase implements Pinenut {

    protected ServiceManager mServiceManager;

    protected GuidAllocator mGuidAllocator;

    protected GrpcServiceMetaTransformer mTransformer;

    public GrpcServiceMetaService( ServiceManager serviceManager ) {
        this.mServiceManager = serviceManager;
        this.mGuidAllocator = serviceManager.getServicesInstrument().getGuidAllocator();
        this.mTransformer = new GrpcServiceMetaTransformer();
    }

    @Override
    public void fetchServicePage( FetchServicePageRequest request, StreamObserver<ServicePageReply> responseObserver ) {
        ServiceElementQuery query = new ServiceElementQuery();
        query.setKeyword( request.getKeyword() );
        query.setOffset( request.getOffset() );
        query.setLimit( request.getLimit() );
        ServiceElementPage page = this.mServiceManager.getServicesInstrument().fetchServicePage( query );

        ServicePageReply.Builder builder = ServicePageReply.newBuilder()
                .setTotal( page.getTotal() )
                .setOffset( (int) page.getOffset() )
                .setLimit( (int) page.getLimit() );
        for ( ServiceElement item : page.getItems() ) {
            builder.addItems( this.mTransformer.toServiceDTO( item ) );
        }
        responseObserver.onNext( builder.build() );
        responseObserver.onCompleted();
    }

    @Override
    public void fetchServiceInstancePage(
            FetchServiceInstancePageRequest request,
            StreamObserver<ServiceInstancePageReply> responseObserver
    ) {
        ServiceInstanceQuery query = new ServiceInstanceQuery();
        if ( !request.getServiceGuid().isBlank() ) {
            query.setServiceGuid( this.mGuidAllocator.parse( request.getServiceGuid() ) );
        }
        query.setStatus( request.getStatus() );
        query.setOffset( request.getOffset() );
        query.setLimit( request.getLimit() );
        ServiceInstancePage page = this.mServiceManager.getServicesInstrument().fetchServiceInstancePage( query );

        ServiceInstancePageReply.Builder builder = ServiceInstancePageReply.newBuilder()
                .setTotal( page.getTotal() )
                .setOffset( (int) page.getOffset() )
                .setLimit( (int) page.getLimit() );
        for ( ServiceInstanceEntry item : page.getItems() ) {
            builder.addItems( this.mTransformer.toServiceInstanceDTO( item ) );
        }
        responseObserver.onNext( builder.build() );
        responseObserver.onCompleted();
    }

    @Override
    public void queryService( QueryServiceRequest request, StreamObserver<ServiceReply> responseObserver ) {
        ServiceElement element = null;
        if ( !request.getPath().isBlank() ) {
            ElementNode node = this.mServiceManager.getServicesInstrument().queryElement( request.getPath() );
            if ( node != null ) {
                element = node.evinceServiceElement();
            }
        }
        if ( element == null && !request.getServiceGuid().isBlank() ) {
            TreeNode node = this.mServiceManager.getServicesInstrument().get(
                    this.mGuidAllocator.parse( request.getServiceGuid() )
            );
            if ( node instanceof ServiceElement ) {
                element = (ServiceElement) node;
            }
        }
        responseObserver.onNext( ServiceReply.newBuilder().setService( this.mTransformer.toServiceDTO( element ) ).build() );
        responseObserver.onCompleted();
    }

    @Override
    public void queryServiceInstance(
            QueryServiceInstanceRequest request,
            StreamObserver<ServiceInstanceReply> responseObserver
    ) {
        ServiceInstanceEntry entry = null;
        if ( !request.getInstanceGuid().isBlank() ) {
            entry = this.mServiceManager.getServicesInstrument().queryServiceInstance(
                    this.mGuidAllocator.parse( request.getInstanceGuid() )
            );
        }
        responseObserver.onNext(
                ServiceInstanceReply.newBuilder().setInstance( this.mTransformer.toServiceInstanceDTO( entry ) ).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void fetchNamespaceChildren(
            FetchNamespaceChildrenRequest request,
            StreamObserver<NamespaceChildrenReply> responseObserver
    ) {
        responseObserver.onNext( NamespaceChildrenReply.newBuilder().build() );
        responseObserver.onCompleted();
    }

    @Override
    public void fetchServiceInsMetaByClientId(
            FetchServiceInsMetaByClientIdRequest request,
            StreamObserver<ServiceMetaListReply> responseObserver
    ) {
        ServiceMetaListReply.Builder builder = ServiceMetaListReply.newBuilder();
        for ( com.pinecone.hydra.service.registry.dto.ServiceMetaDTO item :
                this.mServiceManager.getServiceMetaService().fetchServiceInsMetaByClientId( request.getClientId() ) ) {
            builder.addItems( this.mTransformer.toServiceMetaDTO( item ) );
        }
        responseObserver.onNext( builder.build() );
        responseObserver.onCompleted();
    }

    @Override
    public void fetchServiceInsMetaByServiceId(
            FetchServiceInsMetaByServiceIdRequest request,
            StreamObserver<ServiceMetaListReply> responseObserver
    ) {
        ServiceMetaListReply.Builder builder = ServiceMetaListReply.newBuilder();
        for ( com.pinecone.hydra.service.registry.dto.ServiceMetaDTO item :
                this.mServiceManager.getServiceMetaService().fetchServiceInsMetaByServiceId( request.getServiceId() ) ) {
            builder.addItems( this.mTransformer.toServiceMetaDTO( item ) );
        }
        responseObserver.onNext( builder.build() );
        responseObserver.onCompleted();
    }

    @Override
    public void queryServiceMetaByPath(
            QueryServiceMetaByPathRequest request,
            StreamObserver<ServiceMetaReply> responseObserver
    ) {
        com.pinecone.hydra.service.registry.dto.ServiceMetaDTO meta =
                this.mServiceManager.getServiceMetaService().queryServiceMetaByPath( request.getPath() );
        responseObserver.onNext( ServiceMetaReply.newBuilder().setMeta( this.mTransformer.toServiceMetaDTO( meta ) ).build() );
        responseObserver.onCompleted();
    }

    @Override
    public void queryServiceMetaByGuid(
            QueryServiceMetaByGuidRequest request,
            StreamObserver<ServiceMetaReply> responseObserver
    ) {
        com.pinecone.hydra.service.registry.dto.ServiceMetaDTO meta =
                this.mServiceManager.getServiceMetaService().queryServiceMetaByGuid( request.getGuid() );
        responseObserver.onNext( ServiceMetaReply.newBuilder().setMeta( this.mTransformer.toServiceMetaDTO( meta ) ).build() );
        responseObserver.onCompleted();
    }

    @Override
    public void evalCreationStatement(
            EvalCreationStatementRequest request,
            StreamObserver<GuidReply> responseObserver
    ) {
        String guid = this.mServiceManager.getServiceMetaService().evalCreationStatement( request.getJonsStatement() );
        responseObserver.onNext( GuidReply.newBuilder().setGuid( guid == null ? "" : guid ).build() );
        responseObserver.onCompleted();
    }

    @Override
    public void createNewService(
            CreateNewServiceRequest request,
            StreamObserver<GuidReply> responseObserver
    ) {
        String guid = this.mServiceManager.getServiceMetaService().createNewService(
                request.getParentAppPath(),
                this.mTransformer.toServiceMetaDTO( request.getMeta() )
        );
        responseObserver.onNext( GuidReply.newBuilder().setGuid( guid == null ? "" : guid ).build() );
        responseObserver.onCompleted();
    }
}




