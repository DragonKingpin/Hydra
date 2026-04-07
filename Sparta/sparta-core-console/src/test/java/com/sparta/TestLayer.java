package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.layer.ibatis.hydranium.LayerMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayer;
import com.pinecone.hydra.unit.vgraph.layer.AtlasLayerNamespace;
import com.pinecone.hydra.unit.vgraph.layer.VLayerInstrument;
import com.pinecone.tritium.Tritium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.ArrayList;

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
        //this.testInsert( vLayerManager );
    }

    public void testInsert(VLayerInstrument vLayerManager) {
//        AtlasLayer atlasLayer = new AtlasLayer();
//        atlasLayer.setName("图层1");
//        ArrayList<GUID> sourceGuids = new ArrayList<>();
//        sourceGuids.add( GUIDs.GUID128("01972f7e-1347-7ef4-bdbb-efdc52b7ddf4") );
//        sourceGuids.add( GUIDs.GUID128("01972f7e-15c0-72b8-ad49-41fa8027ca32") );
//        sourceGuids.add( GUIDs.GUID128("01972f7e-15cd-7dcf-8884-88a587ec2c4e") );
//        sourceGuids.add( GUIDs.GUID128("01972f7e-15d9-7565-8ca0-040644fd4493") );
//        sourceGuids.add( GUIDs.GUID128("01972f7e-15e4-73be-863f-02fee71bdc6b") );
//        sourceGuids.add( GUIDs.GUID128("01972f7e-15ee-71a3-8110-7b61c273e7c7") );
//        atlasLayer.setSourceGuids( sourceGuids );
//        atlasLayer.setSourceGuids( sourceGuids );
//
//        ArrayList<GUID> sinkGuids = new ArrayList<>();
//        sinkGuids.add( GUIDs.GUID128("01972f7e-164e-7f80-8e67-a22060a3afd7") );
//        atlasLayer.setSinkGuids( sinkGuids );
//


//        AtlasLayer atlasLayer = new AtlasLayer();
//        atlasLayer.setName("图层11");
//
//        ArrayList<GUID> sourceGuids = new ArrayList<>();
//        sourceGuids.add(GUIDs.GUID128("01972f7e-1347-7ef4-bdbb-efdc52b7ddf4"));
//        sourceGuids.add(GUIDs.GUID128("01972f7e-15c0-72b8-ad49-41fa8027ca32"));
//
//        atlasLayer.setSourceGuids( sourceGuids );
//
//        ArrayList<GUID> sinkGuids = new ArrayList<>();
//        sinkGuids.add(GUIDs.GUID128("01972f7e-164e-7f80-8e67-a22060a3afd7"));
//        atlasLayer.setSinkGuids(sinkGuids);

        AtlasLayer atlasLayer = new AtlasLayer();
        atlasLayer.setName("图层12");

        ArrayList<GUID> sourceGuids = new ArrayList<>();
        sourceGuids.add(GUIDs.GUID128("01972f7e-15cd-7dcf-8884-88a587ec2c4e"));
        sourceGuids.add(GUIDs.GUID128("01972f7e-15d9-7565-8ca0-040644fd4493"));
        sourceGuids.add(GUIDs.GUID128("01972f7e-15e4-73be-863f-02fee71bdc6b"));
        sourceGuids.add(GUIDs.GUID128("01972f7e-15ee-71a3-8110-7b61c273e7c7"));

        atlasLayer.setSourceGuids( sourceGuids );

        ArrayList<GUID> sinkGuids = new ArrayList<>();
        sinkGuids.add(GUIDs.GUID128("01972f7e-164e-7f80-8e67-a22060a3afd7"));
        atlasLayer.setSinkGuids( sinkGuids );

        vLayerManager.put(atlasLayer);

//        AtlasLayerNamespace atlasLayerNamespace = new AtlasLayerNamespace();
//        atlasLayerNamespace.setName("这是测试命名空间");
//        vLayerManager.put( atlasLayerNamespace );
        //vLayerManager.addChild( GUIDs.GUID128("2261a1a-000377-0000-78"), GUIDs.GUID128("2261524-000394-0001-fc") );

    }

    public void testQuery( VLayerInstrument vLayerManager ) {
        // todo 这种情况使用路径取不出
        //Debug.trace(vLayerManager.queryGUIDByPath( "图层1/图层11" ));
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
