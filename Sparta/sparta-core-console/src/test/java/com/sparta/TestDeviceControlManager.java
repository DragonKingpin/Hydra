package com.sparta;

import java.util.List;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.ibatis.hydranium.DeployMappingDriver;
import com.pinecone.hydra.deploy.kom.UniformDeployInstrument;
import com.pinecone.hydra.device.registry.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.registry.constant.DeviceNodeType;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.device.registry.dto.DeviceTopologyDTO;
import com.pinecone.hydra.device.registry.server.DeviceControlManager;
import com.pinecone.hydra.device.registry.server.UniformDeviceControlManager;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
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
        KOIMappingDriver deployMappingDriver = new DeployMappingDriver(
                this,
                ibatisClient,
                this.getDispenserCenter()
        );
        assertTrue(
                ibatisClient.getConfiguration().hasStatement( "com.pinecone.hydra.deploy.ibatis.DeployNamespaceMapper.getGuidsByName" ),
                "deploy namespace mapper XML should be loaded"
        );

        UniformDeployInstrument deployInstrument = new UniformDeployInstrument( deployMappingDriver );
        UniformDeviceControlManager deviceControlManager = new UniformDeviceControlManager( deployInstrument );

        this.testManagerLifecycle( deviceControlManager );
        this.testDeviceLifecycleMetaAndTopology( deviceControlManager );
    }

    private void testManagerLifecycle( UniformDeviceControlManager deviceControlManager ) throws Exception {
        FakeDeviceAppointServer appointServer = new FakeDeviceAppointServer( 10086L );

        deviceControlManager.hookAppointServer( appointServer );
        assertSame( deviceControlManager, appointServer.getDeviceControlManager(), "appoint server should hook manager" );
        assertEquals( 1, deviceControlManager.serverSize(), "appoint server size after hook" );

        deviceControlManager.startDeviceControl();
        assertTrue( appointServer.isStarted(), "appoint server should be started" );

        DeviceAppointServer evicted = deviceControlManager.evictAppointServerById( appointServer.getMessageNodeId() );
        assertSame( appointServer, evicted, "evicted appoint server" );
        assertEquals( 0, deviceControlManager.serverSize(), "appoint server size after evict" );
        assertTrue( !appointServer.isStarted(), "appoint server should be closed after evict" );
    }

    private void testDeviceLifecycleMetaAndTopology( UniformDeviceControlManager deviceControlManager ) {
        String rootPath = "root/deviceControlSmoke" + System.currentTimeMillis();
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
            namespaceGuid = register( deviceControlManager, rootPath, DeviceNodeType.NAMESPACE, "Device Control Smoke Namespace" );
            clusterGuid = register( deviceControlManager, clusterPath, DeviceNodeType.CLUSTER, "Device Control Smoke Cluster" );
            hostGuid = registerPhysicalHost( deviceControlManager, hostPath );
            vmGuid = register( deviceControlManager, vmPath, DeviceNodeType.VIRTUAL_MACHINE, "Device Control Smoke VM" );
            containerGuid = register( deviceControlManager, containerPath, DeviceNodeType.CONTAINER, "Device Control Smoke Container" );

            assertTrue( deviceControlManager.deviceLifecycleService().hasDeviceByPath( hostPath ), "host should exist by path" );
            assertTrue( deviceControlManager.deviceLifecycleService().hasDeviceByGuid( hostGuid.toString() ), "host should exist by guid" );

            DeviceMetaDTO hostMeta = deviceControlManager.deviceMetaService().queryDeviceMetaByPath( hostPath );
            assertEquals( "Acorn", hostMeta.getVendor(), "host vendor" );
            assertEquals( "192.168.50.10", hostMeta.getIpAddress(), "host ip" );

            DeviceMetaDTO updateMeta = new DeviceMetaDTO();
            updateMeta.setStatus( "MAINTENANCE" );
            updateMeta.setIpAddress( "192.168.50.11" );
            updateMeta.setDescription( "Device Control Smoke Host Updated" );
            assertTrue( deviceControlManager.deviceMetaService().updateDeviceMetaByGuid( hostGuid.toString(), updateMeta ), "host meta update" );

            DeviceMetaDTO updatedHostMeta = deviceControlManager.deviceMetaService().queryDeviceMetaByGuid( hostGuid.toString() );
            assertEquals( "Acorn", updatedHostMeta.getVendor(), "partial update should keep vendor" );
            assertEquals( "MAINTENANCE", updatedHostMeta.getStatus(), "partial update should change status" );
            assertEquals( "192.168.50.11", updatedHostMeta.getIpAddress(), "partial update should change ip" );

            assertTrue(
                    deviceControlManager.deviceTopologyService().affirmOwnedRelation(
                            new DeviceTopologyDTO( hostGuid.toString(), vmGuid.toString() )
                    ),
                    "host-vm owned relation"
            );

            List<DeviceMetaDTO> hostChildren = deviceControlManager.deviceTopologyService().fetchChildrenMeta( hostGuid.toString() );
            assertContainsGuid( hostChildren, vmGuid.toString(), "host children should contain vm" );

            List<DeviceMetaDTO> vmChildren = deviceControlManager.deviceTopologyService().fetchChildrenMeta( vmGuid.toString() );
            assertContainsGuid( vmChildren, containerGuid.toString(), "vm children should contain container" );

            Debug.greenfs( "Device control smoke test passed: " + rootPath );
        }
        finally {
            removeIfPresent( deviceControlManager, containerGuid );
            removeIfPresent( deviceControlManager, vmGuid );
            removeIfPresent( deviceControlManager, hostGuid );
            removeIfPresent( deviceControlManager, clusterGuid );
            removeIfPresent( deviceControlManager, namespaceGuid );
        }
    }

    private GUID register( DeviceControlManager deviceControlManager, String path, DeviceNodeType nodeType, String description ) {
        DeviceMetaDTO meta = new DeviceMetaDTO();
        meta.setDescription( description );
        meta.setStatus( "OK" );

        DeviceRegistrationDTO registrationDTO = new DeviceRegistrationDTO();
        registrationDTO.setPath( path );
        registrationDTO.setNodeType( nodeType );
        registrationDTO.setMeta( meta );

        GUID guid = deviceControlManager.registerDevice( registrationDTO );
        assertNotNull( guid, "registered guid for " + path );
        return guid;
    }

    private GUID registerPhysicalHost( DeviceControlManager deviceControlManager, String path ) {
        DeviceMetaDTO meta = new DeviceMetaDTO();
        meta.setAlias( "Smoke Host A" );
        meta.setResourceType( "COMPUTE" );
        meta.setDeviceType( "PHYSICAL_HOST" );
        meta.setVendor( "Acorn" );
        meta.setModel( "SmokeBox-1" );
        meta.setSerialNumber( "SMOKE-HOST-A" );
        meta.setIpAddress( "192.168.50.10" );
        meta.setStatus( "RUNNING" );
        meta.setDescription( "Device Control Smoke Host" );

        DeviceRegistrationDTO registrationDTO = new DeviceRegistrationDTO();
        registrationDTO.setPath( path );
        registrationDTO.setNodeType( DeviceNodeType.PHYSICAL_HOST );
        registrationDTO.setMeta( meta );

        GUID guid = deviceControlManager.registerDevice( registrationDTO );
        assertNotNull( guid, "registered physical host guid" );
        return guid;
    }

    private void removeIfPresent( DeviceControlManager deviceControlManager, GUID guid ) {
        if ( guid != null && deviceControlManager.deviceLifecycleService().hasDeviceByGuid( guid.toString() ) ) {
            deviceControlManager.removeDevice( guid );
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

        private DeviceControlManager deviceControlManager;

        private boolean started;

        private FakeDeviceAppointServer( Long messageNodeId ) {
            this.messageNodeId = messageNodeId;
        }

        @Override
        public Long getMessageNodeId() {
            return this.messageNodeId;
        }

        @Override
        public DeviceAppointServer hookDeviceControlManager( DeviceControlManager deviceControlManager ) {
            this.deviceControlManager = deviceControlManager;
            return this;
        }

        public DeviceControlManager getDeviceControlManager() {
            return this.deviceControlManager;
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

public class TestDeviceControlManager {

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
