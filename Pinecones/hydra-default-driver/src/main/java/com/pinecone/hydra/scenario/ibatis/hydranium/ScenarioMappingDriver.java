package com.pinecone.hydra.scenario.ibatis.hydranium;

import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMasterManipulatorImpl;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class ScenarioMappingDriver extends ArchMappingDriver implements KOIMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;
    public ScenarioMappingDriver(Hydrarum system) {
        super(system);
    }
    public ScenarioMappingDriver(Hydrarum system, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( system, ibatisClient, dispenserCenter, ScenarioMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        this.mKOIMasterManipulator = new ScenarioMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
