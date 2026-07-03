package com.device;

import com.acorn.skynet.device.conduct.DeviceLegionaryJoinRequest;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceLegionary;
import com.acorn.skynet.device.husky.client.HuskyDeviceClientTransport;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.registry.client.UniformDeviceClient;
import com.pinecone.hydra.device.registry.identity.DeviceClientIdentity;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.Tritium;

class DeviceLegionaryClientClara extends Tritium {

    private static final String HUSKY_CLIENT_CONFIG = "{host: \"localhost\",\n" +
            "port: 5772, SocketTimeout: 800, KeepAliveTimeout: 10,\n" +
            "ParallelChannels: 5, AutoReconnect: true, EnableHeartbeat: false, HeartbeatInterval: 2000}";

    protected UniformDeviceInstrument mDeviceInstrument;

    protected UniformDeviceClient mDeviceClient;

    protected SkyCollectiveDeviceLegionary mLegionary;

    public DeviceLegionaryClientClara( String[] args, CascadeSystem parent ) {
        super( args, null, parent );
    }

    @Override
    public void vitalize() throws Exception {
        KOIMappingDriver mappingDriver = new DeviceMappingDriver(
                this,
                (IbatisClient) this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),
                this.getDispenserCenter()
        );

        this.mDeviceInstrument = new UniformDeviceInstrument( mappingDriver );
        GUID deviceGuid = this.prepareTestDeviceMeta();
        long clientId = DeviceClientIdentity.fromDeviceGuid( deviceGuid );
        UlfClient ulfClient = new WolfMCClient( clientId, "", this, new JSONMaptron( HUSKY_CLIENT_CONFIG ) );
        HuskyDeviceClientTransport transport = new HuskyDeviceClientTransport(
                ulfClient,
                this.mDeviceInstrument.getGuidAllocator(),
                null
        );
        this.mDeviceClient = new UniformDeviceClient( deviceGuid, transport );

        DeviceLegionaryJoinRequest joinRequest = new DeviceLegionaryJoinRequest();
        joinRequest.setDeviceGuid( deviceGuid );
        joinRequest.setEndpointProtocol( "Husky" );
        joinRequest.setEndpointHost( "127.0.0.1" );
        joinRequest.setEndpointPort( 5772 );
        joinRequest.setEndpointPath( "/device/clara" );
        joinRequest.setEndpointAddress( "husky://127.0.0.1:5772/device/clara" );
        joinRequest.setMetadataJson( "{source:\"device-legionary-manual\"}" );

        this.mLegionary = new SkyCollectiveDeviceLegionary(
                "test-device-legionary",
                this.mDeviceClient,
                joinRequest
        );
        this.mLegionary.startDevice();
        this.mLegionary.joinRegiment();

        Debug.greenfs( "[DeviceLegionaryClient] clientId => " + clientId );
        Debug.greenfs( "[DeviceLegionaryClient] deviceGuid => " + this.mLegionary.getDeviceGuid() );
        Debug.greenfs( "[DeviceLegionaryClient] instanceGuid => " + this.mLegionary.getInstanceGuid() );
        Debug.greenfs( "[DeviceLegionaryClient] keep running for manual reconnect test." );
    }

    protected GUID prepareTestDeviceMeta() {
        String path = "root/test/device/clara-legionary-host";
        PhysicalHostElement host = this.mDeviceInstrument.affirmPhysicalHost( path );
        host.setAlias( "Clara Legionary Host" );
        host.setIpAddress( "127.0.0.1" );
        host.setStatus( DeviceStatus.New.getName() );
        host.setDescription( "Device legionary manual reconnect test host." );
        this.mDeviceInstrument.update( host );

        PhysicalHostElement queried = (PhysicalHostElement) this.mDeviceInstrument.queryElement( path );
        Debug.greenfs( queried );
        return queried.getGuid();
    }
}

public class TestDeviceLegionary {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            DeviceLegionaryClientClara clara = (DeviceLegionaryClientClara) Pinecone.sys().getTaskManager().add(
                    new DeviceLegionaryClientClara( args, Pinecone.sys() )
            );
            clara.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
