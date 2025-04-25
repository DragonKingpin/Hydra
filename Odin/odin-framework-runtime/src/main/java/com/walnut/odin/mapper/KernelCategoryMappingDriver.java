package com.walnut.odin.mapper;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class KernelCategoryMappingDriver extends ArchMappingDriver implements KOIMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;

    public KernelCategoryMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public KernelCategoryMappingDriver(Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, KernelCategoryMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        this.mKOIMasterManipulator = new CategoryMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
