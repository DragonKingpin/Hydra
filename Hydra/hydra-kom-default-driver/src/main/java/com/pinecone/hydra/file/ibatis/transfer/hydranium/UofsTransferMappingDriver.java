package com.pinecone.hydra.file.ibatis.transfer.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class UofsTransferMappingDriver extends ArchMappingDriver implements KOIMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;

    public UofsTransferMappingDriver( Processum superiorProcess ) {
        super( superiorProcess );
    }

    public UofsTransferMappingDriver(
            Processum superiorProcess,
            IbatisClient ibatisClient,
            ResourceDispenserCenter dispenserCenter
    ) {
        super( superiorProcess, ibatisClient, dispenserCenter, UofsTransferMappingDriver.class.getPackageName().replace( "hydranium", "" ) );
        ibatisClient.addXMLObjectScope( "mapper.kernel.file.transfer" );
        this.mKOIMasterManipulator = new UofsTransferMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }
}
