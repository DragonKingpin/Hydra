package com.pinecone.hydra.business.ibatis.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class MappingDriver extends ArchMappingDriver implements KOIMappingDriver {

    protected KOIMasterManipulator mKOIMasterManipulator;

    public MappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public MappingDriver( Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, MappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        ibatisClient.addXMLObjectScope( "mapper.kernel.business" );
        this.mKOIMasterManipulator = new MasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
