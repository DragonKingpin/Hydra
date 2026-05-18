package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.device.PhysicalHost;
import com.pinecone.hydra.device.entity.GenericPhysicalHost;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.device.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.device.kom.marshaling.DeviceJSONDecoder;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.Tritium;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128;
import com.walnut.archcraft.ender.EnderHydra;


class Randon extends EnderHydra {
    public Randon( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Randon( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new DeviceMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );


        UniformDeviceInstrument deviceInstrument = new UniformDeviceInstrument( koiMappingDriver );

        this.testGet( deviceInstrument );

    }

    private void testInsert(UniformDeviceInstrument instrument) {
/*        String jsonConfig = "{"
                + "name: 'dataSyncJob', "
                + "description: 'Synchronize DB records between clusters', "
                + "extraInformation: 'retries=3; timeout=5000ms', "
                + "enable: false"
                + "}";*/
        String jsonConfig = "{"
                + "name: 'Spark', "
                + "description: 'Track server health metrics in real-time', "
                + "extraInformation: 'interval=60s; alertThreshold=90%', "
                + "enable: false, "
                + "}";


        /*Debug.trace(instrument.queryGUIDByPath("specialTask"));*/
    }

    private void testGet( UniformDeviceInstrument instrument ){
/*        DeviceJSONDecoder decoder = new DeviceJSONDecoder( instrument );
        decoder.decode( new JSONMaptron( "{ root: { test: { cluster: { metaType: ClusterElement, type:Physic, deployments: { vm1: { metaType: VirtualMachineElement, ipAddress: 192.168.1.1, status: 12222s } } } } } }" ) );

        Debug.fmp( 2, instrument.queryElement( "root" ).toJSONObject() );

        Debug.greenfs( instrument.queryElement( "root/test/cluster/vm1" ) );*/

        PhysicalHostElement physicalHost = new GenericPhysicalHostElement();
        physicalHost.setName("testPhysicalHost");
        physicalHost.setIpAddress("127.0.0.1");
        physicalHost.setHardwareSpecs("Intel i7-7700HQ");
        physicalHost.setLocalDomain("localhost");
        physicalHost.setWideDomain("wideDomain");
        physicalHost.setStatus("OK");
        physicalHost.setEnable(true);
        instrument.put(physicalHost);
        //Debug.trace(deviceInstrument.getPath( GUIDs.GUID72("181e9e4-000395-0000-d4") ));
    }

    private void testUpdate( UniformDeviceInstrument instrument ) {

        GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine08");
        virtualMachine.setIpAddress("127.0.0.9");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnable(true);
        virtualMachine.setDescription("testVirtualMachine009");
        virtualMachine.setMetaGuid(GUIDs.GUID128("2261a1a-000377-0000-78"));
        virtualMachine.setGuid(GUIDs.GUID128("24e2fc4-00016c-0000-dc"));
        virtualMachine.setAffiliateHostGuid(GUIDs.GUID128("2261a1a-000377-0000-75"));
        instrument.update( virtualMachine );
    }

    private void testInsertPhysicalHost(UniformDeviceInstrument instrument) {

 /*       GenericPhysicalHostElement physicalHost = new GenericPhysicalHostElement();
        physicalHost.setName("testPhysicalHost");
        physicalHost.setIpAddress("127.0.0.1");
        physicalHost.setHardwareSpecs("Intel i7-7700HQ");
        physicalHost.setStatus("OK");
        physicalHost.setLocalDomain("testDomain");

        //physicalHost.setGuid( GUIDs.GUID72("1b05246-0002cc-0001-f1"));

       *//* instrument.newPhysicalHost(physicalHost);*//*
        instrument.put( physicalHost );
        Debug.info( "physicalHost: " + physicalHost);

*/
/*        GenericQuickElement quickElement = new GenericQuickElement();
        quickElement.setTypeName("testQuickElement009");

       Debug.trace(instrument.put(quickElement)) ;*/
        //测试quick
/*        Debug.trace(instrument.get(GUIDs.GUID72("2508594-0002eb-0000-c0"))) ;
        //测试virtualElement
        Debug.trace(instrument.get(GUIDs.GUID72("24e2fc4-00016c-0000-dc"))) ;
        //测试physicalHost
        Debug.trace(instrument.get(GUIDs.GUID72("2511a12-0003bb-0001-d0"))) ;*/
/*        GenericPhysicalHostElement physicalHost = new GenericPhysicalHostElement( instrument);
        physicalHost.setName("testPhysicalHost");
        physicalHost.setIpAddress("127.0.0.1");
        physicalHost.setHardwareSpecs("Intel i7-7700HQ");
        physicalHost.setStatus("OK");
        physicalHost.setLocalDomain("testDomain");

        instrument.put( physicalHost );*/
     /*   Debug.trace(instrument.get(GUIDs.GUID72("2508b12-000080-0000-58"))) ;
        GenericNamespace  namespace = new GenericNamespace();
        namespace.setName("testNamespace");
        namespace.setDescription("testNamespace");
        namespace.setExtraInformation("testNamespace");
        instrument.put( namespace );*/

/*        TreeNode roodNode = instrument.queryElement("testNamespace" );
        Debug.greenfs("根节点信息: " + roodNode.evinceTreeNode().toJSONString());
        GenericQuickElement taskElement = new GenericQuickElement(
                new JSONMaptron("{ name: '特殊服务9', parentGuid: '" + roodNode.getGuid() + "' }")
        );
        instrument.put(taskElement);*/
/*
        Debug.trace( instrument.(  GUIDs.GUID72("250e136-0002ab-0001-bc")) );
*/
/*        TreeNode root = instrument.queryElement("testNamespace");
       Debug.trace(instrument.getChildren(root.getGuid())) ;*/

        /*Debug.fmp(2, "完整树结构:\n" + root.toJSONString());*/

/*
        Debug.trace(instrument.remove();)
*/
  /*      GenericQuickElement quickElement = new GenericQuickElement();
        quickElement.setName("testQuickElement");
        quickElement.setTypeName("testQuickElement01");
        quickElement.setDescription("testQuickElement02");
        instrument.put( quickElement );*/

/*       Debug.trace( instrument.affirmQuick("testQuickElement") );
       ElementNode quickElement = instrument.queryElement("testQuickElement");
       Debug.trace(quickElement);*/
    }

    private void testInsertVirtualMachine( UniformDeviceInstrument instrument ) {

        GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine01");
        virtualMachine.setIpAddress("127.0.0.5");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnable(true);
        virtualMachine.setDescription("testVirtualMachine");
        virtualMachine.setMetaGuid(GUIDs.GUID128("2261a1a-000377-0000-78"));
        virtualMachine.setGuid(GUIDs.GUID128("2261a1a-000377-0000-76"));
        virtualMachine.setAffiliateHostGuid(GUIDs.GUID128("2261a1a-000377-0000-75"));
        instrument.put( virtualMachine );
   /*     GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine = (GenericVirtualMachineElement)instrument.get( GUIDs.GUID72("24e2fc4-00016c-0000-dc"));
        Debug.trace(virtualMachine);*/

        instrument.get(GUIDs.GUID128("24b1e50-000044-0000-50"));


      /*  GenericVirtualMachine  virtualMachine = new GenericVirtualMachine();
        virtualMachine.setName("VirtualMachine1");
        virtualMachine.setIpAddress("192.168.1.1");
        virtualMachine.setStatus("OK");
        //virtualMachine.setGuid( GUIDs.GUID72("1b05246-0002cc-0001-f2"));
        //virtualMachine.setAffiliateHostGuid(GUIDs.GUID72("1b05246-0002cc-0001-f3"));
        instrument.newVirtualMachine(virtualMachine);
        Debug.info( "virtualMachine: " + virtualMachine);
*/

/*        GenericVirtualMachineElement  virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("VirtualMachine1");
        virtualMachine.setIpAddress("192.168.1.1");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnabled(true);
        virtualMachine.setExtraInformation("extraInformation");
        virtualMachine.setDescription("description");
        virtualMachine.setGuid(GUIDs.GUID72( "1b05246-0002cc-0001-f3"));
        virtualMachine.setMetaGuid(GUIDs.GUID72("1b05246-0002cc-0001-f4"));
        instrument.put(virtualMachine);
        Debug.trace(virtualMachine.toJSONString());*/
    }

}

public class TestDeviceTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Randon Jesse = (Randon) Pinecone.sys().getTaskManager().add( new Randon( args, Pinecone.sys() ) );
            Jesse.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
