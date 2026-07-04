package com.pinecone.hydra.service.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.ibatis.ApplicationNodeMapper;
import com.pinecone.hydra.service.ibatis.ServiceInstanceMapper;
import com.pinecone.hydra.service.ibatis.ServiceNamespaceMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServicePathCacheMapper;
import com.pinecone.hydra.service.ibatis.ServiceRuntimeNodeMapper;
import com.pinecone.hydra.service.ibatis.ServiceTreeMapper;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceRuntimeNodeManipulator;
import com.pinecone.hydra.service.mapper.transaction.ServiceMappingTransaction;
import com.pinecone.hydra.service.mapper.transaction.ServiceTransactionalMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class ServiceMasterManipulatorImpl implements ServiceMasterManipulator {
    protected ServiceTransactionalMappingDriver serviceMappingDriver;

    @Resource
    @Structure(type = ServiceMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    @Resource
    @Structure(type = ServiceTreeMapper.class )
    TrieTreeManipulator             trieTreeManipulator;

    @Resource
    @Structure(type = ApplicationNodeMapper.class )
    ApplicationNodeManipulator      applicationNodeManipulator;

    @Resource
    @Structure( type = ServiceNodeMapper.class )
    ServiceNodeManipulator          serviceNodeManipulator;

    @Resource
    @Structure( type = ServiceNamespaceMapper.class )
    ServiceNamespaceManipulator serviceNamespaceManipulator;

    @Resource
    @Structure( type = ServiceInstanceMapper.class )
    ServiceInstanceManipulator serviceInstanceManipulator;

    @Resource
    @Structure( type = ServiceRuntimeNodeMapper.class )
    ServiceRuntimeNodeManipulator serviceRuntimeNodeManipulator;

    @Resource
    @Structure( type = ServiceNodeOwnerMapper.class )
    TireOwnerManipulator            tireOwnerManipulator;

    public ServiceMasterManipulatorImpl() {

    }

    public ServiceMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( ServiceMasterManipulatorImpl.class, Map.of(), this );
        this.serviceMappingDriver = (ServiceTransactionalMappingDriver) driver;
        this.skeletonMasterManipulator = new ServiceMasterTreeManipulatorImpl( driver );
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.trieTreeManipulator;
    }

    @Override
    public ApplicationNodeManipulator getApplicationNodeManipulator() {
        return this.applicationNodeManipulator;
    }

    @Override
    public ServiceNodeManipulator getServiceNodeManipulator() {
        return this.serviceNodeManipulator;
    }

    @Override
    public ServiceNamespaceManipulator getNamespaceManipulator() {
        return this.serviceNamespaceManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.tireOwnerManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public ServiceInstanceManipulator getServiceInstanceManipulator() {
        return this.serviceInstanceManipulator;
    }

    @Override
    public ServiceRuntimeNodeManipulator getServiceRuntimeNodeManipulator() {
        return this.serviceRuntimeNodeManipulator;
    }

    @Override
    public ServiceMappingTransaction transaction() {
        return this.serviceMappingDriver.transaction();
    }

    @Override
    public void purgeServiceNode( GUID serviceGuid ) {
        this.transaction().required( scope -> {
            scope.mapper( ServiceInstanceMapper.class ).deleteServiceInstancesByServiceGuid( serviceGuid );
            scope.mapper( ServiceRuntimeNodeMapper.class ).deleteServiceRuntimeNodesByServiceGuid( serviceGuid );
            scope.mapper( ServicePathCacheMapper.class ).remove( serviceGuid );
            scope.mapper( ServiceNodeOwnerMapper.class ).removeBySubordinate( serviceGuid );
            scope.mapper( ServiceTreeMapper.class ).removeTreeNode( serviceGuid );
            scope.mapper( ServiceTreeMapper.class ).removeNodeMeta( serviceGuid );
            scope.mapper( ServiceNodeMapper.class ).remove( serviceGuid );
            return null;
        } );
    }

    @Override
    public void purgeServiceDirectory( GUID directoryGuid ) {
        this.transaction().required( scope -> {
            scope.mapper( ServicePathCacheMapper.class ).remove( directoryGuid );
            scope.mapper( ServiceNodeOwnerMapper.class ).removeBySubordinate( directoryGuid );
            scope.mapper( ServiceNamespaceMapper.class ).remove( directoryGuid );
            return null;
        } );
    }
}
