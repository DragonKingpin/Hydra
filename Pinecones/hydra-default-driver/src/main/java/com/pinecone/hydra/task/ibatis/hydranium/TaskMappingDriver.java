package com.pinecone.hydra.task.ibatis.hydranium;

import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.scenario.ibatis.hydranium.ScenarioMappingDriver;
import com.pinecone.hydra.scenario.ibatis.hydranium.ScenarioMasterManipulatorImpl;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class TaskMappingDriver extends ArchMappingDriver implements KOIMappingDriver {

    protected KOIMasterManipulator mKOIMasterManipulator;
    public TaskMappingDriver(Hydrarum system) {
        super(system);
    }
    public TaskMappingDriver(Hydrarum system, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( system, ibatisClient, dispenserCenter, TaskMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        this.mKOIMasterManipulator = new TaskMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
