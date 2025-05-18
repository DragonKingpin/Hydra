package com.ender;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.system.ko.runtime.GenericRuntimeInstrumentConfig;
import com.pinecone.hydra.system.ko.runtime.KernelExpressInstrument;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;

class Floki extends EnderHydra {
    public Floki( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Floki( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        OdinUniformTaskMappingDriver categoryMappingDriver = new OdinUniformTaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        RavenTaskInstrument ravenTaskInstrument = new RavenTaskInstrument( categoryMappingDriver );


        KernelExpressInstrument kernelExpressInstrument = new KernelExpressInstrument( "", new GenericRuntimeInstrumentConfig());
        kernelExpressInstrument.setTargetingName("task1");
        kernelExpressInstrument.mount( "task1/afc", ravenTaskInstrument );

        this.testSimple( kernelExpressInstrument );
    }

    private void testSimple( KernelExpressInstrument instrument ) {
        EntityNode entityNode = instrument.queryNode( "task1/afc/test/job/task" );

        //Debug.fmp( 2, entityNode );
        Debug.fmp( 2, instrument.querySystemKernelObjectPath( entityNode.getGuid() ) );

        Debug.fmp( 2, instrument.getMountedInstrument( "task1/afc" ) );
    }
}

public class TestEnderHydra {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Floki loki = (Floki) Pinecone.sys().getTaskManager().add( new Floki( args, Pinecone.sys() ) );
            loki.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
