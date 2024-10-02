package com.walnut.sparta.services.drivers;

import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.service.ibatis.AppNodeMetaMapper;
import com.pinecone.hydra.service.ibatis.ApplicationNodeMapper;
import com.pinecone.hydra.service.ibatis.ClassifNodeMapper;
import com.pinecone.hydra.service.ibatis.ClassifRulesMapper;
import com.pinecone.hydra.service.ibatis.ServiceCommonDataMapper;
import com.pinecone.hydra.service.ibatis.ServiceFamilyTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceMetaMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import com.pinecone.hydra.service.ibatis.ServiceTrieTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServicePathCacheMapper;
import org.springframework.stereotype.Component;


@Component
public class ServiceMasterManipulatorImpl implements ServiceMasterManipulator {
    @Resource
    @Structure(type = ServiceTrieTreeMapper.class)
    private ServiceTrieTreeMapper          trieTreeManipulator;
    @Resource
    @Structure(type = ServiceCommonDataMapper.class)
    private CommonDataManipulator          commonDataManipulator;

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
    @Structure(type = ClassifNodeMapper.class)
    private ClassifNodeManipulator         classifNodeManipulator;

    @Resource
    @Structure(type = ClassifRulesMapper.class)
    private ClassifRulesManipulator        classifRulesManipulator;


    @Resource
    @Structure(type = ServiceFamilyTreeMapper.class)
    private ServiceFamilyTreeManipulator   serviceFamilyTreeManipulator;


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
    public CommonDataManipulator getCommonDataManipulator() {
        return this.commonDataManipulator;
    }

    @Override
    public ApplicationNodeManipulator getApplicationNodeManipulator() {
        return this.applicationNodeManipulator;
    }

    @Override
    public ApplicationMetaManipulator getApplicationMetaManipulator() {
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
    public ClassifNodeManipulator getClassifNodeManipulator() {
        return this.classifNodeManipulator;
    }

    @Override
    public ClassifRulesManipulator getClassifRulesManipulator() {
        return this.classifRulesManipulator;
    }

    @Override
    public ServiceFamilyTreeManipulator getServiceFamilyTreeManipulator() {
        return this.serviceFamilyTreeManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.scopeOwnerManipulator;
    }


    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }
}
