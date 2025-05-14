package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.deploy.entity.GenericPhysicalHost;
import com.pinecone.hydra.deploy.ibatis.hydranium.DeployMappingDriver;
import com.pinecone.hydra.deploy.kom.UniformDeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployElement;
import com.pinecone.hydra.deploy.kom.entity.GenericPhysicalHostElement;
import com.pinecone.hydra.deploy.kom.entity.GenericQuickElement;
import com.pinecone.hydra.deploy.kom.entity.GenericVirtualMachineElement;
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
       this.testInsertPhysicalHost( deployInstrument );



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

        GenericDeployElement taskElement = new GenericDeployElement(
                new JSONMaptron( jsonConfig )
        );

        taskElement.setMetaGuid(GUIDs.GUID72("2b05246-0002cc-0002-f2"));
        Debug.info( "taskElement: " + taskElement );
        instrument.put(taskElement);
        /*Debug.trace(instrument.queryGUIDByPath("specialTask"));*/
    }

    private void testGet( UniformDeployInstrument instrument ) {
        Debug.info( "taskElement: " + instrument.get(GUIDs.GUID72("24b2258-0000bd-0000-44")));
    }
    private void testUpdate( UniformDeployInstrument instrument ) {

        GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine08");
        virtualMachine.setIpAddress("127.0.0.9");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnabled(true);
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
      instrument.get(GUIDs.GUID72("24e2fc4-00016c-0000-dc"));


/*
        Debug.trace(instrument.remove();)
*/
    }

    private void testInsertVirtualMachine(UniformDeployInstrument instrument) {

        GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine.setName("testVirtualMachine01");
        virtualMachine.setIpAddress("127.0.0.5");
        virtualMachine.setStatus("OK");
        virtualMachine.setEnabled(true);
        virtualMachine.setDescription("testVirtualMachine");
        virtualMachine.setMetaGuid(GUIDs.GUID72("2261a1a-000377-0000-78"));
        virtualMachine.setGuid(GUIDs.GUID72("2261a1a-000377-0000-76"));
        virtualMachine.setAffiliateHostGuid(GUIDs.GUID72("2261a1a-000377-0000-75"));
        instrument.put( virtualMachine );
   /*     GenericVirtualMachineElement virtualMachine = new GenericVirtualMachineElement();
        virtualMachine = (GenericVirtualMachineElement)instrument.get( GUIDs.GUID72("24e2fc4-00016c-0000-dc"));
        Debug.trace(virtualMachine);*/
        GenericDeployElement taskElement = new GenericDeployElement();
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
