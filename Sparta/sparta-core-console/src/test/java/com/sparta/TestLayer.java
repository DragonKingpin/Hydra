package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.layer.ibatis.hydranium.LayerMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayerNamespace;
import com.pinecone.hydra.unit.vgraph.layer.VLayerInstrument;
import com.pinecone.tritium.Tritium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;

class Louis extends Tritium {
    public Louis( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Louis( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new LayerMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        VLayerInstrument vLayerManager = new VLayerInstrument(koiMappingDriver);
        this.testQuery(vLayerManager);
    }

    public void testInsert(VLayerInstrument vLayerManager) {
        AtlasLayer atlasLayer = new AtlasLayer();
        atlasLayer.setName("这是测试图层");
        vLayerManager.put(atlasLayer);

        AtlasLayerNamespace atlasLayerNamespace = new AtlasLayerNamespace();
        atlasLayerNamespace.setName("这是测试命名空间");
        vLayerManager.put( atlasLayerNamespace );
        //vLayerManager.addChild( GUIDs.GUID128("2261a1a-000377-0000-78"), GUIDs.GUID128("2261524-000394-0001-fc") );

    }

    public void testQuery( VLayerInstrument vLayerManager ) {
        Debug.trace(vLayerManager.queryGUIDByPath( "这是测试命名空间/这是测试图层" ));
    }
}

public class TestLayer {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Louis louis = (Louis) Pinecone.sys().getTaskManager().add( new Louis( args, Pinecone.sys() ) );
            louis.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
