package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.business.BusinessInstrument;
import com.pinecone.hydra.business.UniformBusinessInstrument;
import com.pinecone.hydra.business.ibatis.hydranium.MappingDriver;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.tritium.Tritium;


class LadyGaga extends Tritium {
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


    }

    private void testService(){
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        UniformServiceInstrument distributedScopeServiceTree = new UniformServiceInstrument(koiMappingDriver);

        //Debug.trace(distributedScopeServiceTree.getNode(GUIDs.GUID72("f83ccfc-0002f9-0000-b4")).toString());
        Debug.trace(distributedScopeServiceTree.getPath(GUIDs.GUID128("f83ccfc-0002f9-0000-b4")));
    }

    private void testBusiness(){
        KOIMappingDriver koiMappingDriver = new MappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        BusinessInstrument businessInstrument = new UniformBusinessInstrument( koiMappingDriver );
//        GenericBusinessNode genericBusinessNode = new GenericBusinessNode();
//        genericBusinessNode.setName("瘟疫公司");
//        genericBusinessNode.setBusinessNodeMeta(new GenericBusinessNodeMeta());
//        genericBusinessNode.setBusinessCommonData(new GenericBusinessCommonData());
//        distributedBusinessMetaTree.insert(genericBusinessNode);
        businessInstrument.get( GUIDs.GUID128( "1f5bced8-000315-0002-70" ) );
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
