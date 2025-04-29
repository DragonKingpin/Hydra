package com.sparta;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.hydra.layer.ibatis.hydranium.LayerMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.vgraph.layer.VLayerManager;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

class Peter extends Radium {
    public Peter( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Peter( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new LayerMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        //VLayerManager vLayerManager = new VLayerManager(koiMappingDriver,null);

    }

}
public class TestDAG {
}
