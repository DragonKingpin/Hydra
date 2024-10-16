package com.pinecone.hydra.service.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMasterManipulatorImpl;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMasterTreeManipulatorImpl;
import com.pinecone.hydra.service.ibatis.AppNodeMetaMapper;
import com.pinecone.hydra.service.ibatis.ApplicationNodeMapper;
import com.pinecone.hydra.service.ibatis.ClassifNodeMapper;
import com.pinecone.hydra.service.ibatis.ClassifRulesMapper;
import com.pinecone.hydra.service.ibatis.ServiceCommonDataMapper;
import com.pinecone.hydra.service.ibatis.ServiceFamilyTreeMapper;
import com.pinecone.hydra.service.ibatis.ServiceMetaMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeMapper;
import com.pinecone.hydra.service.ibatis.ServiceNodeOwnerMapper;
import com.pinecone.hydra.service.ibatis.ServiceTrieTreeMapper;
import com.pinecone.hydra.service.tree.source.ApplicationMetaManipulator;
import com.pinecone.hydra.service.tree.source.ApplicationNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifNodeManipulator;
import com.pinecone.hydra.service.tree.source.ClassifRulesManipulator;
import com.pinecone.hydra.service.tree.source.CommonDataManipulator;
import com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMasterManipulator;
import com.pinecone.hydra.service.tree.source.ServiceMetaManipulator;
import com.pinecone.hydra.service.tree.source.ServiceNodeManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Map;
@Component
public class ServiceMasterManipulatorImpl implements ServiceMasterManipulator {

    @Resource
    @Structure(type = ServiceMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;
    @Resource
    @Structure(type = ServiceTrieTreeMapper.class )
    TrieTreeManipulator             trieTreeManipulator;
    @Resource
    @Structure(type = ServiceCommonDataMapper.class )
    CommonDataManipulator           commonDataManipulator;
    @Resource
    @Structure(type = ApplicationNodeMapper.class )
    ApplicationNodeManipulator      applicationNodeManipulator;
    @Resource
    @Structure( type = AppNodeMetaMapper.class )
    ApplicationMetaManipulator      applicationMetaManipulator;
    @Resource
    @Structure( type = ServiceNodeMapper.class )
    ServiceNodeManipulator          serviceNodeManipulator;
    @Resource
    @Structure( type = ServiceMetaMapper.class )
    ServiceMetaManipulator          serviceMetaManipulator;
    @Resource
    @Structure( type = ClassifNodeMapper.class )
    ClassifNodeManipulator          classifNodeManipulator;
    @Resource
    @Structure( type = ClassifRulesMapper.class )
    ClassifRulesManipulator         classifRulesManipulator;
    @Resource
    @Structure( type = ServiceFamilyTreeMapper.class )
    ServiceFamilyTreeManipulator    serviceFamilyTreeManipulator;
    @Resource
    @Structure( type = ServiceNodeOwnerMapper.class )
    TireOwnerManipulator            tireOwnerManipulator;

    public ServiceMasterManipulatorImpl() {

    }

    public ServiceMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( ServiceMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new ServiceMasterTreeManipulatorImpl( driver );
    }

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
        return this.tireOwnerManipulator;
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }
}
