package com.service.auto;

import com.acorn.redqueen.service.registry.grpc.client.GrpcServiceClientTransport;
import com.acorn.redqueen.service.registry.grpc.client.GrpcServiceClientTransportConfig;
import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlTransportFactory;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;
import com.pinecone.hydra.grpc.client.GrpcClientConfig;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;

public class GrpcServiceLegionaryScenario implements ServiceLegionaryTransportScenario {

    protected static final int Port = 5668;

    protected GrpcAppointServer mServer;

    protected GrpcAppointClient mClient;

    protected GrpcServiceClientTransport mTransport;

    @Override
    public String name() {
        return "Grpc";
    }

    @Override
    public String endpointProtocol() {
        return "grpc";
    }

    @Override
    public int endpointPort() {
        return Port;
    }

    @Override
    public void hookServerTransport( ServiceLegionarySmokeContext context ) throws Exception {
        GrpcServerConfig config = new GrpcServerConfig( new JSONMaptron(
                "{host:\"0.0.0.0\", port:" + Port + ", enableHeartbeat:true, heartbeatIntervalMillis:500, keepAliveTimeSec:1, keepAliveTimeoutSec:1}"
        ) );
        this.mServer = new GrpcAppointServer( "service-legionary-grpc-server", Port, config, context.system );
        context.serviceManager.transportRegistry().hookTransport(
                new GrpcServiceControlTransportFactory().create( context.serviceManager, this.mServer )
        );
    }

    @Override
    public UniformServiceClient createServiceClient( ServiceLegionarySmokeContext context ) throws Exception {
        GrpcClientConfig clientConfig = new GrpcClientConfig( new JSONMaptron(
                "{host:\"127.0.0.1\", port:" + Port + ", enableHeartbeat:true, heartbeatIntervalMillis:500, keepAliveTimeSec:1, keepAliveTimeoutSec:1}"
        ) );
        this.mClient = new GrpcAppointClient(
                "service-legionary-grpc-client",
                new GuidAllocator72V2().nextGUIDi64(),
                clientConfig
        );
        GrpcServiceClientTransportConfig transportConfig = new GrpcServiceClientTransportConfig( new JSONMaptron(
                "{controlSyncTimeoutMillis:5000, commandTimeoutMillis:10000, enableHeartbeat:true, heartbeatIntervalMillis:500}"
        ) );
        this.mTransport = new GrpcServiceClientTransport(
                this.mClient,
                context.serviceInstrument.getGuidAllocator(),
                transportConfig
        );
        return new UniformServiceClient( context.serviceInstrument.getGuidAllocator(), this.mTransport );
    }

    @Override
    public void breakClientConnection( ServiceLegionarySmokeContext context ) {
        if ( this.mClient != null ) {
            this.mClient.close();
        }
    }

    @Override
    public void requestStateSynchronization( ServiceLegionarySmokeContext context, String szReason ) {
        if ( this.mTransport != null ) {
            this.mTransport.requestControlStateSynchronization( szReason );
        }
    }

    @Override
    public void cleanup( ServiceLegionarySmokeContext context ) {
        if ( this.mClient != null ) {
            this.mClient.close();
        }
        if ( this.mServer != null ) {
            this.mServer.close();
        }
    }
}
