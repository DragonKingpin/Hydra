package com.sparta;

import java.util.List;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.registry.client.HuskyDeviceClient;
import com.pinecone.hydra.device.registry.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.device.registry.dto.DeviceTopologyDTO;
import com.pinecone.hydra.device.registry.server.DeviceLifecycleIface;
import com.pinecone.hydra.device.registry.server.DeviceMetaManipulationIface;
import com.pinecone.hydra.device.registry.server.DeviceTopologyManipulationIface;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.UniformDeviceManager;
import com.pinecone.hydra.device.registry.ulf.HuskyDeviceAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;
import com.walnut.archcraft.ender.EnderHydra;

class Selene extends EnderHydra {

    public Selene( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Selene( String[] args, String szName, CascadeSystem parent ) {
        super( args, szName, parent );
    }

    @Override
    public void vitalize() throws Exception {
        IbatisClient ibatisClient = (IbatisClient) this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" );
        KOIMappingDriver deviceMappingDriver = new DeviceMappingDriver(
                this,
                ibatisClient,
                this.getDispenserCenter()
        );
        assertTrue(
                ibatisClient.getConfiguration().hasStatement( "com.pinecone.hydra.device.ibatis.DeviceNamespaceMapper.getGuidsByName" ),
                "device namespace mapper XML should be loaded"
        );

        UniformDeviceInstrument deviceInstrument = new UniformDeviceInstrument( deviceMappingDriver );
        UniformDeviceManager deviceManager = new UniformDeviceManager( deviceInstrument );

        this.testManagerLifecycle( deviceManager );
        this.testDeviceLifecycleMetaAndTopology( deviceInstrument, deviceManager );
        this.testDeviceWolfHuskyRPC( deviceInstrument );
    }

    private void testManagerLifecycle( UniformDeviceManager deviceManager ) throws Exception {
        FakeDeviceAppointServer appointServer = new FakeDeviceAppointServer( 10086L );

        deviceManager.hookAppointServer( appointServer );
        assertSame( deviceManager, appointServer.getDeviceManager(), "appoint server should hook manager" );
        assertEquals( 1, deviceManager.serverSize(), "appoint server size after hook" );

        deviceManager.startDeviceManager();
        assertTrue( appointServer.isStarted(), "appoint server should be started" );

        DeviceAppointServer evicted = deviceManager.evictAppointServerById( appointServer.getMessageNodeId() );
        assertSame( appointServer, evicted, "evicted appoint server" );
        assertEquals( 0, deviceManager.serverSize(), "appoint server size after evict" );
        assertTrue( !appointServer.isStarted(), "appoint server should be closed after evict" );
    }

    private void testDeviceLifecycleMetaAndTopology( DeviceInstrument deviceInstrument, UniformDeviceManager deviceManager ) {
        String rootPath = "deviceControlSmoke" + System.currentTimeMillis();
        String clusterPath = rootPath + "/clusterA";
        String hostPath = clusterPath + "/hostA";
        String vmPath = hostPath + "/vmA";
        String containerPath = vmPath + "/containerA";

        GUID namespaceGuid = null;
        GUID clusterGuid = null;
        GUID hostGuid = null;
        GUID vmGuid = null;
        GUID containerGuid = null;

        try {
            namespaceGuid = deviceInstrument.affirmNamespace( rootPath ).getGuid();
            clusterGuid = deviceInstrument.affirmCluster( clusterPath ).getGuid();
            hostGuid = deviceInstrument.affirmPhysicalHost( hostPath ).getGuid();
            vmGuid = deviceInstrument.affirmVirtualMachine( vmPath ).getGuid();
            containerGuid = deviceInstrument.affirmContainerElement( containerPath ).getGuid();

            enroll( deviceManager, rootPath, "Device Manager Smoke Namespace" );
            enroll( deviceManager, clusterPath, "Device Manager Smoke Cluster" );
            enrollPhysicalHost( deviceManager, hostPath );
            enroll( deviceManager, vmPath, "Device Manager Smoke VM" );
            enroll( deviceManager, containerPath, "Device Manager Smoke Container" );

            assertTrue( deviceManager.deviceLifecycleService().hasDeviceByPath( hostPath ), "host should exist by path" );
            assertTrue( deviceManager.deviceLifecycleService().hasDeviceByGuid( hostGuid.toString() ), "host should exist by guid" );

            DeviceMetaDTO hostMeta = deviceManager.deviceMetaService().queryDeviceMetaByPath( hostPath );
            assertEquals( "Acorn", hostMeta.getVendor(), "host vendor" );
            assertEquals( "192.168.50.10", hostMeta.getIpAddress(), "host ip" );

            DeviceMetaDTO updateMeta = new DeviceMetaDTO();
            updateMeta.setStatus( "MAINTENANCE" );
            updateMeta.setIpAddress( "192.168.50.11" );
            updateMeta.setDescription( "Device Manager Smoke Host Updated" );
            assertTrue( deviceManager.deviceMetaService().updateDeviceMetaByGuid( hostGuid.toString(), updateMeta ), "host meta update" );

            DeviceMetaDTO updatedHostMeta = deviceManager.deviceMetaService().queryDeviceMetaByGuid( hostGuid.toString() );
            assertEquals( "Acorn", updatedHostMeta.getVendor(), "partial update should keep vendor" );
            assertEquals( "MAINTENANCE", updatedHostMeta.getStatus(), "partial update should change status" );
            assertEquals( "192.168.50.11", updatedHostMeta.getIpAddress(), "partial update should change ip" );

            assertTrue(
                    deviceManager.deviceTopologyService().affirmOwnedRelation(
                            new DeviceTopologyDTO( hostGuid.toString(), vmGuid.toString() )
                    ),
                    "host-vm owned relation"
            );

            List<DeviceMetaDTO> hostChildren = deviceManager.deviceTopologyService().fetchChildrenMeta( hostGuid.toString() );
            assertContainsGuid( hostChildren, vmGuid.toString(), "host children should contain vm" );

            List<DeviceMetaDTO> vmChildren = deviceManager.deviceTopologyService().fetchChildrenMeta( vmGuid.toString() );
            assertContainsGuid( vmChildren, containerGuid.toString(), "vm children should contain container" );

            Debug.greenfs( "Device manager smoke test passed: " + rootPath );
        }
        finally {
            removeIfPresent( deviceManager, containerGuid );
            removeIfPresent( deviceManager, vmGuid );
            removeIfPresent( deviceManager, hostGuid );
            removeIfPresent( deviceManager, clusterGuid );
            removeIfPresent( deviceManager, namespaceGuid );
        }
    }

    private void testDeviceWolfHuskyRPC( DeviceInstrument deviceInstrument ) throws Exception {
        String rootPath = "deviceRPCSmoke" + System.currentTimeMillis();
        String clusterPath = rootPath + "/clusterA";
        String hostPath = clusterPath + "/hostA";
        String vmPath = hostPath + "/vmA";
        String containerPath = vmPath + "/containerA";

        GUID namespaceGuid = null;
        GUID clusterGuid = null;
        GUID hostGuid = null;
        GUID vmGuid = null;
        GUID containerGuid = null;

        UniformDeviceManager deviceManager = new UniformDeviceManager( deviceInstrument );
        HuskyDeviceClient managerClient = null;

        try {
            namespaceGuid = deviceInstrument.affirmNamespace( rootPath ).getGuid();
            clusterGuid = deviceInstrument.affirmCluster( clusterPath ).getGuid();
            hostGuid = deviceInstrument.affirmPhysicalHost( hostPath ).getGuid();
            vmGuid = deviceInstrument.affirmVirtualMachine( vmPath ).getGuid();
            containerGuid = deviceInstrument.affirmContainerElement( containerPath ).getGuid();

            WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron( "{host: \"0.0.0.0\", port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}" ) );
            deviceManager.hookAppointServer( new HuskyDeviceAppointServer( new WolvesAppointServer( wolfKing, HuskyDuplexExpress.class ) ) );
            deviceManager.startDeviceManager();

            UlfClient ulfClient = new WolfMCClient(
                    new GuidAllocator72V2().nextGUIDi64(),
                    "",
                    this,
                    this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
            );
            managerClient = new HuskyDeviceClient( ulfClient );
            managerClient.startService();

            DeviceLifecycleIface lifecycleIface = managerClient.deviceLifecycleIface();
            DeviceMetaManipulationIface metaIface = managerClient.deviceMetaManipulationIface();
            DeviceTopologyManipulationIface topologyIface = managerClient.deviceTopologyManipulationIface();

            enroll( lifecycleIface, rootPath, "Device RPC Smoke Namespace" );
            enroll( lifecycleIface, clusterPath, "Device RPC Smoke Cluster" );
            enrollPhysicalHost( lifecycleIface, hostPath );
            enroll( lifecycleIface, vmPath, "Device RPC Smoke VM" );
            enroll( lifecycleIface, containerPath, "Device RPC Smoke Container" );

            assertTrue( lifecycleIface.hasDeviceByPath( hostPath ), "rpc host should exist by path" );
            assertTrue( lifecycleIface.hasDeviceByGuid( hostGuid.toString() ), "rpc host should exist by guid" );

            DeviceMetaDTO hostMeta = metaIface.queryDeviceMetaByPath( hostPath );
            assertEquals( "Acorn", hostMeta.getVendor(), "rpc host vendor" );

            DeviceMetaDTO updateMeta = new DeviceMetaDTO();
            updateMeta.setStatus( "RPC_MAINTENANCE" );
            updateMeta.setIpAddress( "192.168.60.11" );
            assertTrue( metaIface.updateDeviceMetaByGuid( hostGuid.toString(), updateMeta ), "rpc host meta update" );

            DeviceMetaDTO updatedHostMeta = metaIface.queryDeviceMetaByGuid( hostGuid.toString() );
            assertEquals( "Acorn", updatedHostMeta.getVendor(), "rpc partial update should keep vendor" );
            assertEquals( "RPC_MAINTENANCE", updatedHostMeta.getStatus(), "rpc partial update should change status" );
            assertEquals( "192.168.60.11", updatedHostMeta.getIpAddress(), "rpc partial update should change ip" );

            assertTrue(
                    topologyIface.affirmOwnedRelation( new DeviceTopologyDTO( hostGuid.toString(), vmGuid.toString() ) ),
                    "rpc host-vm owned relation"
            );
            assertContainsGuid( topologyIface.fetchChildrenMeta( hostGuid.toString() ), vmGuid.toString(), "rpc host children should contain vm" );
            assertContainsGuid( topologyIface.fetchChildrenMeta( vmGuid.toString() ), containerGuid.toString(), "rpc vm children should contain container" );

            Debug.greenfs( "Device Wolf-Husky RPC smoke test passed: " + rootPath );
        }
        finally {
            if ( managerClient != null ) {
                managerClient.terminateService();
            }
            for ( DeviceAppointServer appointServer : deviceManager.getServers() ) {
                appointServer.close();
            }
            removeIfPresent( deviceManager, containerGuid );
            removeIfPresent( deviceManager, vmGuid );
            removeIfPresent( deviceManager, hostGuid );
            removeIfPresent( deviceManager, clusterGuid );
            removeIfPresent( deviceManager, namespaceGuid );
        }
    }

    private GUID enroll( DeviceManager deviceManager, String path, String description ) {
        DeviceMetaDTO meta = new DeviceMetaDTO();
        meta.setDescription( description );
        meta.setStatus( "OK" );

        DeviceRegistrationDTO registrationDTO = new DeviceRegistrationDTO();
        registrationDTO.setPath( path );
        registrationDTO.setMeta( meta );

        GUID guid = deviceManager.enrollDevice( registrationDTO );
        assertNotNull( guid, "enrolled guid for " + path );
        return guid;
    }

    private String enroll( DeviceLifecycleIface lifecycleIface, String path, String description ) {
        DeviceMetaDTO meta = new DeviceMetaDTO();
        meta.setDescription( description );
        meta.setStatus( "OK" );

        DeviceRegistrationDTO registrationDTO = new DeviceRegistrationDTO();
        registrationDTO.setPath( path );
        registrationDTO.setMeta( meta );

        String guid = lifecycleIface.enrollDevice( registrationDTO );
        assertNotNull( guid, "rpc enrolled guid for " + path );
        return guid;
    }

    private GUID enrollPhysicalHost( DeviceManager deviceManager, String path ) {
        DeviceMetaDTO meta = new DeviceMetaDTO();
        meta.setAlias( "Smoke Host A" );
        meta.setResourceType( "COMPUTE" );
        meta.setDeviceType( "PHYSICAL_HOST" );
        meta.setVendor( "Acorn" );
        meta.setModel( "SmokeBox-1" );
        meta.setSerialNumber( "SMOKE-HOST-A" );
        meta.setIpAddress( "192.168.50.10" );
        meta.setStatus( "RUNNING" );
        meta.setDescription( "Device Manager Smoke Host" );

        DeviceRegistrationDTO registrationDTO = new DeviceRegistrationDTO();
        registrationDTO.setPath( path );
        registrationDTO.setMeta( meta );

        GUID guid = deviceManager.enrollDevice( registrationDTO );
        assertNotNull( guid, "enrolled physical host guid" );
        return guid;
    }

    private String enrollPhysicalHost( DeviceLifecycleIface lifecycleIface, String path ) {
        DeviceMetaDTO meta = new DeviceMetaDTO();
        meta.setAlias( "Smoke Host A" );
        meta.setResourceType( "COMPUTE" );
        meta.setDeviceType( "PHYSICAL_HOST" );
        meta.setVendor( "Acorn" );
        meta.setModel( "SmokeBox-1" );
        meta.setSerialNumber( "SMOKE-HOST-A" );
        meta.setIpAddress( "192.168.60.10" );
        meta.setStatus( "RUNNING" );
        meta.setDescription( "Device RPC Smoke Host" );

        DeviceRegistrationDTO registrationDTO = new DeviceRegistrationDTO();
        registrationDTO.setPath( path );
        registrationDTO.setMeta( meta );

        String guid = lifecycleIface.enrollDevice( registrationDTO );
        assertNotNull( guid, "rpc enrolled physical host guid" );
        return guid;
    }

    private void removeIfPresent( DeviceManager deviceManager, GUID guid ) {
        if ( guid != null && deviceManager.deviceLifecycleService().hasDeviceByGuid( guid.toString() ) ) {
            deviceManager.removeDevice( guid );
        }
    }

    private void assertContainsGuid( List<DeviceMetaDTO> metas, String guid, String message ) {
        for ( DeviceMetaDTO meta : metas ) {
            if ( guid.equals( meta.getGuid() ) ) {
                return;
            }
        }
        throw new AssertionError( message + ": " + guid );
    }

    private void assertNotNull( Object actual, String message ) {
        if ( actual == null ) {
            throw new AssertionError( message );
        }
    }

    private void assertTrue( boolean actual, String message ) {
        if ( !actual ) {
            throw new AssertionError( message );
        }
    }

    private void assertEquals( Object expected, Object actual, String message ) {
        if ( expected == null ? actual != null : !expected.equals( actual ) ) {
            throw new AssertionError( message + ", expected: " + expected + ", actual: " + actual );
        }
    }

    private void assertSame( Object expected, Object actual, String message ) {
        if ( expected != actual ) {
            throw new AssertionError( message );
        }
    }

    private static class FakeDeviceAppointServer implements DeviceAppointServer {

        private final Long messageNodeId;

        private DeviceManager deviceManager;

        private boolean started;

        private FakeDeviceAppointServer( Long messageNodeId ) {
            this.messageNodeId = messageNodeId;
        }

        @Override
        public Long getMessageNodeId() {
            return this.messageNodeId;
        }

        @Override
        public DeviceAppointServer hookDeviceManager( DeviceManager deviceManager ) {
            this.deviceManager = deviceManager;
            return this;
        }

        public DeviceManager getDeviceManager() {
            return this.deviceManager;
        }

        @Override
        public void execute() {
            this.started = true;
        }

        @Override
        public boolean isStarted() {
            return this.started;
        }

        @Override
        public void close() {
            this.started = false;
        }
    }
}

public class TestDeviceManager {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            Selene selene = (Selene) Pinecone.sys().getTaskManager().add(
                    new Selene( args, Pinecone.sys() )
            );
            selene.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
