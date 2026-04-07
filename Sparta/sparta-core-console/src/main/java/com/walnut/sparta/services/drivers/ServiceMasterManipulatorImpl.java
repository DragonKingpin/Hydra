package com.walnut.sparta.services.drivers;

import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.service.ibatis.AppNodeMetaMapper;
import com.pinecone.hydra.service.ibatis.ApplicationNodeMapper;
import com.pinecone.hydra.service.ibatis.ServiceInstanceMapper;
import com.pinecone.hydra.service.ibatis.ServiceNamespaceMapper;
import com.pinecone.hydra.service.ibatis.NamespaceRulesMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeMetaMapper;
import com.pinecone.hydra.service.ibatis.ServiceMetaMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeMapper;
import com.pinecone.hydra.service.kom.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.kom.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceInstanceManipulator;
import com.pinecone.hydra.service.kom.source.ServiceNamespaceManipulator;
import com.pinecone.hydra.service.kom.source.NamespaceRulesManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.kom.source.NodeMetaManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMetaManipulator;
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
    @Structure(type = ServiceNodeMetaMapper.class)
    private NodeMetaManipulator nodeMetaManipulator;

    @Resource
    @Structure(type = ApplicationNodeMapper.class)
    private ApplicationNodeManipulator     applicationNodeManipulator;
    @Resource
    @Structure(type = AppNodeMetaMapper.class)
    private ApplicationMetaManipulator     applicationMetaManipulator;

    @Resource
    @Structure(type = ServiceNodeMapper.class)
    private ServiceNodeManipulator         serviceNodeManipulator;
    @Resource
    @Structure(type = ServiceMetaMapper.class)
    private ServiceMetaManipulator         serviceMetaManipulator;

    @Resource
    @Structure(type = ServiceNamespaceMapper.class)
    private ServiceNamespaceManipulator serviceNamespaceManipulator;

    @Resource
    @Structure(type = ServiceInstanceMapper.class)
    private ServiceInstanceManipulator serviceInstanceManipulator;

    @Resource
    @Structure(type = NamespaceRulesMapper.class)
    private NamespaceRulesManipulator namespaceRulesManipulator;

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
    public NodeMetaManipulator getNodeMetaManipulator() {
        return this.nodeMetaManipulator;
    }

    @Override
    public ApplicationNodeManipulator getApplicationNodeManipulator() {
        return this.applicationNodeManipulator;
    }

    @Override
    public ApplicationMetaManipulator getApplicationElementManipulator() {
        return this.applicationMetaManipulator;
    }

    @Override
    public ServiceNodeManipulator getServiceNodeManipulator() {
        return this.serviceNodeManipulator;
    }

    @Override
    public ServiceMetaManipulator getServiceMetaManipulator() {
        return this.serviceMetaManipulator;
    }

    @Override
    public ServiceNamespaceManipulator getNamespaceManipulator() {
        return this.serviceNamespaceManipulator;
    }

    @Override
    public NamespaceRulesManipulator getNamespaceRulesManipulator() {
        return this.namespaceRulesManipulator;
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
