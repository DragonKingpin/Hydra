package com.device;

import com.acorn.skynet.device.conduct.SkyCollectiveDeviceRegiment;
import com.acorn.skynet.device.husky.client.HuskyDeviceClientTransport;
import com.acorn.skynet.device.husky.server.HuskyDeviceControlTransport;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.device.DeviceStatus;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.client.UniformDeviceClient;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.identity.DeviceClientIdentity;
import com.pinecone.hydra.device.registry.server.UniformDeviceManager;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.Tritium;

class Clara extends Tritium {

    private static final String HUSKY_CLIENT_CONFIG = "{host: \"localhost\",\n" +
            "port: 5771, SocketTimeout: 800, KeepAliveTimeout: 10,\n" +
            "ParallelChannels: 5, AutoReconnect: true, EnableHeartbeat: false, HeartbeatInterval: 2000}";

    protected static final int Port = 5772;

    protected UniformDeviceInstrument deviceInstrument;

    protected UniformDeviceManager deviceManager;

    protected WolfMCServer server;

    protected SkyCollectiveDeviceRegiment deviceRegiment;

    protected GUID deviceGuid;

    protected UniformDeviceClient primaryClient;

    public Clara( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Clara( String[] args, String name, CascadeSystem parent ) {
        super( args, name, parent );
    }

    @Override
    public void vitalize() throws Exception {
        KOIMappingDriver mappingDriver = new DeviceMappingDriver(
                this,
                (IbatisClient) this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),
                this.getDispenserCenter()
        );

        this.deviceInstrument = new UniformDeviceInstrument( mappingDriver );
        this.server = new WolfMCServer( "", this, new JSONMaptron(
                "{host:\"0.0.0.0\", port:" + Port + ", SocketTimeout:800, KeepAliveTimeout:3600, MaximumConnections:1000000}"
        ) );
        this.deviceManager = new UniformDeviceManager( this.deviceInstrument );
        this.deviceManager.hookTransport( new HuskyDeviceControlTransport( new WolvesAppointServer( this.server ) ) );
        this.deviceRegiment = new SkyCollectiveDeviceRegiment( this.deviceInstrument, this.deviceManager );
        //this.deviceRegiment.startDeviceManager();




        this.deviceGuid = this.testDeviceMetaCreation();
        this.primaryClient = this.createDeviceClient( this.deviceGuid );
        this.primaryClient.startDevice();

        this.testDeviceRegister_Proactive( this.primaryClient, this.deviceGuid );
//        this.testDuplicateRegister( this.primaryClient );
//        this.testExclusiveRuntime( this.deviceGuid );
//        this.testDeregister( this.primaryClient );

//        this.primaryClient = this.createDeviceClient( this.deviceGuid );
//        this.primaryClient.startDevice();
//        this.testDetach( this.primaryClient );

//        this.cleanup();
    }

    protected GUID testDeviceMetaCreation() {
        String path = "root/test/device/clara-host";
        PhysicalHostElement host = this.deviceInstrument.affirmPhysicalHost( path );
        host.setAlias( "Clara Host" );
        host.setIpAddress( "127.0.0.1" );
        host.setStatus( DeviceStatus.New.getName() );
        host.setDescription( "Device manager runtime smoke host." );
        this.deviceInstrument.update( host );

        PhysicalHostElement queried = (PhysicalHostElement) this.deviceManager.queryDeviceByPath( path );
        Debug.greenfs( queried );
        return queried.getGuid();
    }

    protected void testDeviceRegister_Proactive( UniformDeviceClient client, GUID deviceGuid ) throws Exception {
        DeviceClientRegisterResult result = client.registerDevice();
        DeviceInstanceEntry runtime = this.deviceManager.deviceRuntimeService().queryDeviceRuntime( deviceGuid );
        Debug.greenfs( result );
        Debug.bluefs( runtime );
        Debug.bluefs( this.deviceManager.deviceRuntimeService().fetchDeviceRuntimes() );
    }

    protected void testDuplicateRegister( UniformDeviceClient client ) throws Exception {
        DeviceClientRegisterResult result = client.registerDevice();
        Debug.greenfs( result );
        Debug.bluefs( this.deviceManager.deviceRuntimeService().fetchDeviceRuntimes() );
    }

    protected void testExclusiveRuntime( GUID deviceGuid ) throws Exception {
        UniformDeviceClient anotherClient = this.createDeviceClient( deviceGuid );
        anotherClient.startDevice();
        try {
            Debug.redfs( anotherClient.registerDevice() );
        }
        catch ( Exception e ) {
            Debug.redfs( e.getMessage() );
        }
        finally {
            anotherClient.terminateDevice();
        }
        Debug.bluefs( this.deviceManager.deviceRuntimeService().fetchDeviceRuntimes() );
    }

    protected void testDeregister( UniformDeviceClient client ) throws Exception {
        client.deregisterDevice( "ClaraDeregisterTest" );
        Debug.redfs( "After deregister:" );
        Debug.bluefs( this.deviceManager.deviceRuntimeService().fetchDeviceRuntimes() );
        client.terminateDevice();
    }

    protected void testDetach( UniformDeviceClient client ) throws Exception {
        DeviceClientRegisterResult result = client.registerDevice();
        Debug.greenfs( result );
        client.terminateDevice();
        Debug.redfs( "After detach:" );
        Debug.bluefs( this.deviceManager.deviceRuntimeService().fetchDeviceRuntimes() );
    }

    protected UniformDeviceClient createDeviceClient( GUID deviceGuid ) {
        UlfClient rpcClient = new WolfMCClient(
                DeviceClientIdentity.fromDeviceGuid( deviceGuid ),
                "",
                this,
                new JSONMaptron( "{host:\"127.0.0.1\", port:" + Port + ", SocketTimeout:800, KeepAliveTimeout:10, AutoReconnect: true, MaximumConnections:1000000, ParallelChannels:3}" )
        );
        return new UniformDeviceClient( deviceGuid, new HuskyDeviceClientTransport( rpcClient ) );
    }

    protected void cleanup() {
        if ( this.primaryClient != null ) {
            this.primaryClient.terminateDevice();
        }
        if ( this.server != null ) {
            this.server.close();
        }
    }
}

public class TestDeviceManager {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            Clara clara = (Clara) Pinecone.sys().getTaskManager().add( new Clara( args, Pinecone.sys() ) );
            clara.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
