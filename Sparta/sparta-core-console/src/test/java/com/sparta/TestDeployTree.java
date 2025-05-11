package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.deploy.DeployExtraMeta;
import com.pinecone.hydra.deploy.entity.GenericPhysicalHost;
import com.pinecone.hydra.deploy.entity.GenericVirtualMachine;
import com.pinecone.hydra.deploy.ibatis.hydranium.DeployMappingDriver;
import com.pinecone.hydra.deploy.kom.UniformDeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.GenericDeployElement;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.radium.Radium;
import com.pinecone.ulf.util.guid.GUID72;
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
        this.testInsertVirtualMachine( deployInstrument );

    }
    private void testInsert(UniformDeployInstrument instrument) {
/*        String jsonConfig = "{"
                + "name: 'dataSyncJob', "
                + "description: 'Synchronize DB records between clusters', "
                + "extraInformation: 'retries=3; timeout=5000ms', "
                + "enable: false"
                + "}";*/
        String jsonConfig = "{"
                + "name: 'healthMonitor', "
                + "description: 'Track server health metrics in real-time', "
                + "extraInformation: 'interval=60s; alertThreshold=90%', "
                + "enable: true, "
                + "}";

        GenericDeployElement taskElement = new GenericDeployElement(
                new JSONMaptron( jsonConfig )
        );
        taskElement.setMetaGuid(GUIDs.GUID72("1b05246-0002cc-0001-f6"));
        Debug.info( "taskElement: " + taskElement );
        instrument.put(taskElement);
    }

    private void testInsertPhysicalHost(UniformDeployInstrument instrument) {

        GenericPhysicalHost  physicalHost = new GenericPhysicalHost();
        physicalHost.setName("testPhysicalHost");
        physicalHost.setIpAddress("127.0.0.1");
        physicalHost.setHardwareSpecs("Intel i7-7700HQ");
        physicalHost.setStatus("OK");
        physicalHost.setGuid( GUIDs.GUID72("1b05246-0002cc-0001-f1"));

        instrument.newPhysicalHost(physicalHost);
        Debug.info( "physicalHost: " + physicalHost);

    }

    private void testInsertVirtualMachine(UniformDeployInstrument instrument) {


        GenericVirtualMachine  virtualMachine = new GenericVirtualMachine();
        virtualMachine.setName("VirtualMachine1");
        virtualMachine.setIpAddress("192.168.1.1");
        virtualMachine.setStatus("OK");
        virtualMachine.setGuid( GUIDs.GUID72("1b05246-0002cc-0001-f2"));
        virtualMachine.setAffiliateHostGuid(GUIDs.GUID72("1b05246-0002cc-0001-f3"));
        instrument.newVirtualMachine(virtualMachine);
        Debug.info( "virtualMachine: " + virtualMachine);

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
