package com.pinecone.hydra.atlas.runtime.ibatis.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.atlas.graph.ibatis.hydranium.ArchAtlasMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class RuntimeMappingDriver extends ArchAtlasMappingDriver implements AtlasMappingDriver {
    protected AtlasMasterManipulator mVectorGraphMasterManipulator;

    public RuntimeMappingDriver( Processum superiorProcess ){
        super( superiorProcess );
    }

    public RuntimeMappingDriver(Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, RuntimeMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        this.mVectorGraphMasterManipulator = new RuntimeMasterManipulatorImpl( this );
    }

    @Override
    public AtlasMasterManipulator getMasterManipulator() {
        return this.mVectorGraphMasterManipulator;
    }
}
