package com.pinecone.hydra.queue.ibatis.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class QueueMappingDriver extends ArchMappingDriver implements KOIMappingDriver {

    protected KOIMasterManipulator mKOIMasterManipulator;

    public QueueMappingDriver(Processum superiorProcess) {
        super(superiorProcess);
    }

    public QueueMappingDriver(Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, QueueMappingDriver.class.getPackageName().replace( "hydranium", "" ) );
        this.mKOIMasterManipulator = new QueueMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
