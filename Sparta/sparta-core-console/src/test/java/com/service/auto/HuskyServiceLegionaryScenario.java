package com.service.auto;

import com.acorn.redqueen.service.registry.husky.client.HuskyServiceClientTransport;
import com.acorn.redqueen.service.registry.husky.server.HuskyServiceControlTransportFactory;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;

public class HuskyServiceLegionaryScenario implements ServiceLegionaryTransportScenario {

    protected static final int Port = 5667;

    protected WolfMCServer mServer;

    protected UlfClient mClient;

    protected HuskyServiceClientTransport mTransport;

    protected long mnClientId;

    @Override
    public String name() {
        return "Husky";
    }

    @Override
    public String endpointProtocol() {
        return "husky";
    }

    @Override
    public int endpointPort() {
        return Port;
    }

    @Override
    public void hookServerTransport( ServiceLegionarySmokeContext context ) throws Exception {
        this.mServer = new WolfMCServer(
                "",
                context.system,
                new JSONMaptron( "{host:\"0.0.0.0\", port:" + Port + ", SocketTimeout:800, KeepAliveTimeout:3600, MaximumConnections:1000000}" )
        );
        context.serviceManager.transportRegistry().hookTransport(
                HuskyServiceControlTransportFactory.create( context.serviceManager, this.mServer )
        );
    }

    @Override
    public UniformServiceClient createServiceClient( ServiceLegionarySmokeContext context ) throws Exception {
        this.mnClientId = new GuidAllocator72V2().nextGUIDi64();
        this.mClient = this.createRPCClient( context );
        this.mTransport = new HuskyServiceClientTransport(
                this.mClient,
                context.serviceInstrument.getGuidAllocator(),
                null,
                () -> this.createRPCClient( context )
        );
        return new UniformServiceClient( context.serviceInstrument.getGuidAllocator(), this.mTransport );
    }

    protected UlfClient createRPCClient( ServiceLegionarySmokeContext context ) {
        this.mClient = new WolfMCClient(
                this.mnClientId,
                "",
                context.system,
                new JSONMaptron( "{host:\"127.0.0.1\", port:" + Port + ", SocketTimeout:800, KeepAliveTimeout:3600, MaximumConnections:1000000, ParallelChannels:3}" )
        );
        return this.mClient;
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
