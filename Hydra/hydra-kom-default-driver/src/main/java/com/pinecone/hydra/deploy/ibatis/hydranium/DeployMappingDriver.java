package com.pinecone.hydra.deploy.ibatis.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class DeployMappingDriver extends ArchMappingDriver implements KOIMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;

    public DeployMappingDriver(Processum superiorProcess  ) {
        super( superiorProcess );
    }

    // Temp , TODO
    public DeployMappingDriver(Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, DeployMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        this.mKOIMasterManipulator = new DeployMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
