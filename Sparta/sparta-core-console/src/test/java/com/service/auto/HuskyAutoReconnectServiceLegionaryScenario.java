package com.service.auto;

import com.acorn.redqueen.service.registry.husky.client.HuskyServiceClientTransport;
import com.acorn.redqueen.service.registry.husky.server.HuskyServiceControlTransportFactory;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;

public class HuskyAutoReconnectServiceLegionaryScenario implements ServiceLegionaryTransportScenario {

    protected static final int Port = 5671;

    protected WolfMCServer mServer;

    protected UlfClient mClient;

    protected long mnClientId;

    @Override
    public String name() {
        return "HuskyAutoReconnect";
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
        this.mClient = new WolfMCClient(
                this.mnClientId,
                "",
                context.system,
                new JSONMaptron(
                        "{host:\"127.0.0.1\", port:" + Port
                                + ", SocketTimeout:3000, KeepAliveTimeout:10, ParallelChannels:3, AutoReconnect:true, EnableHeartbeat:false, HeartbeatInterval:2000}"
                )
        );
        HuskyServiceClientTransport transport = new HuskyServiceClientTransport(
                this.mClient,
                context.serviceInstrument.getGuidAllocator(),
                null
        );
        return new UniformServiceClient( context.serviceInstrument.getGuidAllocator(), transport );
    }

    @Override
    public void breakClientConnection( ServiceLegionarySmokeContext context ) {
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
