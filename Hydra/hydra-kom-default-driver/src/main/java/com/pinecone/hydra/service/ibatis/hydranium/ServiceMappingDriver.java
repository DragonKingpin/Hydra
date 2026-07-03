package com.pinecone.hydra.service.ibatis.hydranium;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.entity.ibatis.hydranium.ArchMappingDriver;
import com.pinecone.hydra.system.component.ResourceDispenserCenter;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.service.ibatis.transaction.IbatisServiceMappingTransaction;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransaction;
import com.pinecone.hydra.service.mapper.transaction.ServiceTransactionalMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

public class ServiceMappingDriver extends ArchMappingDriver implements KOIMappingDriver, ServiceTransactionalMappingDriver {
    protected KOIMasterManipulator mKOIMasterManipulator;
    protected ServiceMappingTransaction mTransaction;

    public ServiceMappingDriver( Processum superiorProcess ) {
        super(superiorProcess);
    }

    public ServiceMappingDriver( Processum superiorProcess, IbatisClient ibatisClient, ResourceDispenserCenter dispenserCenter ) {
        super( superiorProcess, ibatisClient, dispenserCenter, ServiceMappingDriver.class.getPackageName().replace( "hydranium", "" ) );

        ibatisClient.addXMLObjectScope( "mapper.kernel.service" );
        this.mTransaction = new IbatisServiceMappingTransaction( ibatisClient );
        this.mKOIMasterManipulator = new ServiceMasterManipulatorImpl( this );
    }

    @Override
    public KOIMasterManipulator getMasterManipulator() {
        return this.mKOIMasterManipulator;
    }

    @Override
    public ServiceMappingTransaction transaction() {
        return this.mTransaction;
    }
}
