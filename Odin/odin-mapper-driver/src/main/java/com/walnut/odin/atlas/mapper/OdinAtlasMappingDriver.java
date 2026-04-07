package com.walnut.odin.atlas.mapper;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class OdinAtlasMappingDriver extends ArchAtlasMappingDriver implements AtlasMappingDriver {
    protected AtlasMasterManipulator mVectorGraphMasterManipulator;

    public OdinAtlasMappingDriver( Processum superiorProcess ){
        super( superiorProcess );
    }

    public OdinAtlasMappingDriver( Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, OdinAtlasMappingDriver.class.getPackageName() );

        this.mVectorGraphMasterManipulator = new OdinAtlasMasterManipulatorImpl( this );
    }

    @Override
    public AtlasMasterManipulator getMasterManipulator() {
        return this.mVectorGraphMasterManipulator;
    }
}
