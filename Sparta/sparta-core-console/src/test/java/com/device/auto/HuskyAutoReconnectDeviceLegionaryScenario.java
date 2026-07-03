package com.device.auto;

import com.acorn.skynet.device.husky.client.HuskyDeviceClientTransport;
import com.acorn.skynet.device.husky.server.HuskyDeviceControlTransportFactory;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.device.registry.client.UniformDeviceClient;
import com.pinecone.hydra.device.registry.identity.DeviceClientIdentity;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;

public class HuskyAutoReconnectDeviceLegionaryScenario implements DeviceLegionaryTransportScenario {

    protected static final int Port = 5672;

    protected WolfMCServer server;

    protected UlfClient client;

    protected HuskyDeviceClientTransport transport;

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
    public void hookServerTransport( DeviceLegionarySmokeContext context ) throws Exception {
        this.server = new WolfMCServer(
                "",
                context.system,
                new JSONMaptron( "{host:\"0.0.0.0\", port:" + Port + ", SocketTimeout:800, KeepAliveTimeout:3600, MaximumConnections:1000000}" )
        );
        context.deviceManager.addTransport(
                new HuskyDeviceControlTransportFactory().create( context.deviceManager, new WolvesAppointServer( this.server ) )
        );
    }

    @Override
    public UniformDeviceClient createDeviceClient( DeviceLegionarySmokeContext context ) throws Exception {
        long clientId = DeviceClientIdentity.fromDeviceGuid( context.deviceGuid );
        this.client = this.createRPCClient( context, clientId );
        this.transport = new HuskyDeviceClientTransport(
                this.client,
                context.deviceInstrument.getGuidAllocator(),
                () -> this.createRPCClient( context, clientId )
        );
        return new UniformDeviceClient( context.deviceGuid, this.transport );
    }

    protected UlfClient createRPCClient( DeviceLegionarySmokeContext context, long clientId ) {
        this.client = new WolfMCClient(
                clientId,
                "",
                context.system,
                new JSONMaptron(
                        "{host:\"127.0.0.1\", port:" + Port
                                + ", SocketTimeout:3000, KeepAliveTimeout:10, ParallelChannels:3, AutoReconnect:true, EnableHeartbeat:false, HeartbeatInterval:2000}"
                )
        );
        return this.client;
    }

    @Override
    public void requestStateSynchronization( DeviceLegionarySmokeContext context, String reason ) {
        if ( this.transport != null ) {
            this.transport.requestControlStateSynchronization( reason );
        }
    }

    @Override
    public void cleanup( DeviceLegionarySmokeContext context ) {
        if ( this.client != null ) {
            this.client.close();
        }
        if ( this.server != null ) {
            this.server.close();
        }
    }
}
