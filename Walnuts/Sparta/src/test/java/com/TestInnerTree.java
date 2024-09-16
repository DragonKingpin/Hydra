package com;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.GenericDistributeRegistry;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.id.GUID72;
import com.sauron.radium.Radium;


class LadyGaga extends Radium {
    public LadyGaga( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public LadyGaga( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
//        Sparta sparta = new Sparta( "Sparta", this );
//        sparta.execute();
//
//        Thread shutdowner = new Thread(()->{
//            Debug.sleep( 5000 );
//            sparta.terminate();
//        });
//        //shutdowner.start();
//
//        this.getTaskManager().add( sparta );
//        this.getTaskManager().syncWaitingTerminated();



        this.testRegistry();
    }

    private void testRegistry() {
        KOIMappingDriver koiMappingDriver = new RegistryMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        DistributedRegistry distributedRegistry = new GenericDistributeRegistry( koiMappingDriver );

        Debug.trace( distributedRegistry.getPath( new GUID72( "1f391ed2-0002d8-0000-e4" ) ) );
        Debug.trace( distributedRegistry.get( new GUID72( "1f419c8c-000018-0000-a8" ) ).toString() );
    }
}


public class TestInnerTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            LadyGaga ladyGaga = (LadyGaga) Pinecone.sys().getTaskManager().add( new LadyGaga( args, Pinecone.sys() ) );
            ladyGaga.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
