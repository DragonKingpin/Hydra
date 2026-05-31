package com.walnut.sparta.services.drivers;

import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.service.ibatis.ApplicationNodeMapper;
import com.pinecone.hydra.service.ibatis.ServiceInstanceMapper;
import com.pinecone.hydra.service.ibatis.ServiceNamespaceMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeMapper;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNodeManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import com.pinecone.hydra.service.ibatis.ServiceTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServicePathCacheMapper;
import org.springframework.stereotype.Component;


@Component
public class ServiceMasterManipulatorImpl implements ServiceMasterManipulator {
    @Resource
    @Structure(type = ServiceTreeMapper.class)
    private ServiceTreeMapper trieTreeManipulator;

    @Resource
    @Structure(type = ApplicationNodeMapper.class)
    private ApplicationNodeManipulator     applicationNodeManipulator;

    @Resource
    @Structure(type = ServiceNodeMapper.class)
    private ServiceNodeManipulator         serviceNodeManipulator;

    @Resource
    @Structure(type = ServiceNamespaceMapper.class)
    private ServiceNamespaceManipulator serviceNamespaceManipulator;

    @Resource
    @Structure(type = ServiceInstanceMapper.class)
    private ServiceInstanceManipulator serviceInstanceManipulator;

    @Resource
    @Structure(type = ServiceNodeOwnerMapper.class)
    private ServiceNodeOwnerMapper          scopeOwnerManipulator;

    @Resource
    @Structure(type = ServicePathCacheMapper.class)
    private ServicePathCacheMapper scopePathManipulator;

    @Resource( type = ServiceMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator    skeletonMasterManipulator;


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
        return this.scopeOwnerManipulator;
    }


    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public ServiceInstanceManipulator getServiceInstanceManipulator() {
        return this.serviceInstanceManipulator;
    }
}
