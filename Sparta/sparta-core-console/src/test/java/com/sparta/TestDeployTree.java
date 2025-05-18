package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.deploy.ibatis.hydranium.DeployMappingDriver;
import com.pinecone.hydra.deploy.kom.UniformDeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
import com.pinecone.hydra.deploy.kom.marshaling.DeployJSONDecoder;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.radium.Radium;
import com.pinecone.ulf.util.guid.GUIDs;


class Randon extends Radium {
    public Randon( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Randon( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        DeployMappingDriver deployMappingDriver = new DeployMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        UniformDeployInstrument deployInstrument = new UniformDeployInstrument( deployMappingDriver );
        /*this.testInsert( deployInstrument );*/
        /*this.testUpdate( deployInstrument );*/
        //  this.testInsertPhysicalHost( deployInstrument );
        /*this.testUpdate( deployInstrument );*/
/*         deployInstrument.affirmNamespace( "testNamespace").addChild(new GenericNamespace( new JSONMaptron( "{ name: weapon002, description: d1 }" )));
         GenericQuickElement quickElement = new GenericQuickElement();
         quickElement.setName("weapon005");
         quickElement.setTypeName("weapon");
         deployInstrument.put(quickElement);*/
        /*TreeNode roodNode = deployInstrument.queryElement("testNamespace/weapon001" );*/
        /*Debug.greenfs("根节点信息: " + roodNode.evinceTreeNode().toJSONString());*/
     /*   GenericNamespace namespace = new GenericNamespace();
        namespace.setName("testNamespace007");
        namespace.setDescription("testNamespace007");
        deployInstrument.put(namespace);
        TreeNode roodNode1 = deployInstrument.queryElement("testNamespace006" );
        Debug.greenfs("根节点信息: " + roodNode1.evinceTreeNode().toJSONString());*/
/*       GenericQuickElement  quickElement = new GenericQuickElement();
        quickElement.setName("quick0067889");
        quickElement.setTypeName("quick123889");
        deployInstrument.put(quickElement);
        TreeNode  quickElementNode = deployInstrument.queryElement("testNamespace006" );*/
/*
        Debug.greenfs("根节点信息: " + deployInstrument.queryElement( "quick0067889" ));
         Debug.fmp( 2, deployInstrument.queryElement( "testNamespace/weapon001" ).evinceNamespace().toJSONDetails() );

*/
/*       GenericDeployElement  deployElement = new GenericDeployElement();
       deployElement.setName("deployElement004");
       deployElement.setEnable( true);
       deployInstrument.put(deployElement);*/
        /*Debug.greenfs("根节点信息: " + deployInstrument.queryElement( "VirtualMachine1" ));*/

/*        GenericPhysicalHostElement physicalHost = new GenericPhysicalHostElement();
        physicalHost.setName("testPhysicalHost00123");
        physicalHost.setIpAddress("127.0.0.1");
        physicalHost.setHardwareSpecs("Intel i7-7700HQ");
        physicalHost.setLocalDomain("localhost");
        physicalHost.setWideDomain("wideDomain");
        physicalHost.setStatus("OK");
        physicalHost.setEnable(true);
        deployInstrument.put(physicalHost);
        Debug.greenfs("根节点信息: " + deployInstrument.queryElement( "testPhysicalHost00123" ));*/

/*        TreeNode node = deployInstrument.queryElement("deployElement002" );
        Debug.greenfs("根节点信息: " + node.evinceTreeNode().toJSONString());*/
/*        GenericVirtualMachineElement  virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine009");
        virtualMachine.setIpAddress("127.0.0.9");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnable(true);
        deployInstrument.put(virtualMachine);
        Debug.greenfs("根节点信息: " + deployInstrument.queryElement( "testVirtualMachine009" ));*/
/*        GenericQuickElement  quickElement = new GenericQuickElement();
        quickElement.setName("quickElement009");
        quickElement.setTypeName("quickElement009");
        deployInstrument.put(quickElement);*/
       // Debug.greenfs("根节点信息: " + deployInstrument.queryElement( "quickElement009" ));
        //deployInstrument.affirmQuick("quickElement003").addChild(new GenericQuickElement( new JSONMaptron( "{ name: 'deployElement002', description: 'd1' }" )));
/*        GenericDeployElement deployElement = new GenericDeployElement();
        deployElement.setName("deployElement004");
        deployElement.setEnable(true);
        deployInstrument.put(deployElement);
        Debug.greenfs("根节点信息: " + deployInstrument.queryElement( "deployElement004" ));
        deployInstrument.affirmDeployNode("deployElement004").addChild(new GenericDeployElement( new JSONMaptron( "{ name: 'deployElement003', description: 'd1' }" )));*/


        this.testGet( deployInstrument );

    }

    private void testInsert(UniformDeployInstrument instrument) {
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

    private void testGet( UniformDeployInstrument instrument ){
        DeployJSONDecoder decoder = new DeployJSONDecoder( instrument );
        decoder.decode( new JSONMaptron( "{ root: { test: { cluster: { metaType: ClusterElement, type:Physic, deployments: { vm1: { metaType: VirtualMachineElement, ipAddress: 192.168.1.1, status: 12222s } } } } } }" ) );

        Debug.fmp( 2, instrument.queryElement( "root" ).toJSONObject() );

        Debug.greenfs( instrument.queryElement( "root/test/cluster/vm1" ) );
        //Debug.trace(deployInstrument.getPath( GUIDs.GUID72("181e9e4-000395-0000-d4") ));
    }

    private void testUpdate( UniformDeployInstrument instrument ) {

        GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine08");
        virtualMachine.setIpAddress("127.0.0.9");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnable(true);
        virtualMachine.setDescription("testVirtualMachine009");
        virtualMachine.setMetaGuid(GUIDs.GUID72("2261a1a-000377-0000-78"));
        virtualMachine.setGuid(GUIDs.GUID72("24e2fc4-00016c-0000-dc"));
        virtualMachine.setAffiliateHostGuid(GUIDs.GUID72("2261a1a-000377-0000-75"));
        instrument.update( virtualMachine );
    }

    private void testInsertPhysicalHost(UniformDeployInstrument instrument) {

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

    private void testInsertVirtualMachine( UniformDeployInstrument instrument ) {

        GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine01");
        virtualMachine.setIpAddress("127.0.0.5");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnable(true);
        virtualMachine.setDescription("testVirtualMachine");
        virtualMachine.setMetaGuid(GUIDs.GUID72("2261a1a-000377-0000-78"));
        virtualMachine.setGuid(GUIDs.GUID72("2261a1a-000377-0000-76"));
        virtualMachine.setAffiliateHostGuid(GUIDs.GUID72("2261a1a-000377-0000-75"));
        instrument.put( virtualMachine );
   /*     GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine = (GenericVirtualMachineElement)instrument.get( GUIDs.GUID72("24e2fc4-00016c-0000-dc"));
        Debug.trace(virtualMachine);*/

        instrument.get(GUIDs.GUID72("24b1e50-000044-0000-50"));


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

public class TestDeployTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Randon Jesse = (Randon) Pinecone.sys().getTaskManager().add( new Randon( args, Pinecone.sys() ) );
            Jesse.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
