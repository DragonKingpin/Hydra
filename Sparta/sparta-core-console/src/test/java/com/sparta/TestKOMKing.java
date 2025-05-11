package com.sparta;


import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.hydra.system.ko.runtime.KernelExpressInstrument;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;

class Loki extends Radium {
    public Loki( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Loki( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        OdinUniformTaskMappingDriver categoryMappingDriver = new OdinUniformTaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        RavenTaskInstrument ravenTaskInstrument = new RavenTaskInstrument( categoryMappingDriver );


        KernelExpressInstrument kernelExpressInstrument = new KernelExpressInstrument( "", null );
    }

    private void testCategory( KernelExpressInstrument instrument ) {

    }
}

public class TestKOMKing {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Loki loki = (Loki) Pinecone.sys().getTaskManager().add( new Loki( args, Pinecone.sys() ) );
            loki.vitalize();
            return 0;
        }, (Object[]) args );
    }
}