package com.pinecone.hydra.lifecycle.ibatis.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class StorageLifecycleMappingDriver extends ArchMappingDriver implements KOIMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;

    public StorageLifecycleMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public StorageLifecycleMappingDriver(
            Processum superiorProcess,
            IbatisClient ibatisClient,
            ResourceDispenserCenter dispenserCenter
    ) {
        super( superiorProcess, ibatisClient, dispenserCenter, StorageLifecycleMappingDriver.class.getPackageName().replace( "hydranium", "" ) );
        ibatisClient.addXMLObjectScope( "mapper.kernel.storage" );

        this.mKOIMasterManipulator = new StorageLifecycleMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
