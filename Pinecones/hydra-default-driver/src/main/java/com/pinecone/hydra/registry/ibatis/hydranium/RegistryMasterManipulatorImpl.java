package com.pinecone.hydra.registry.ibatis.hydranium;

import java.util.Map;
import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.registry.ibatis.RegistryCommonDataMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNSNodeMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNSNodeMetaMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNodeMapper;
import com.pinecone.hydra.registry.ibatis.RegistryNodeMetaMapper;
import com.pinecone.hydra.registry.ibatis.RegistryPropertiesMapper;
import com.pinecone.hydra.registry.ibatis.RegistryTextValueMapper;
import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;

public class RegistryMasterManipulatorImpl implements RegistryMasterManipulator {
    @Resource
    @Structure( type = RegistryNodeMapper.class )
    RegistryNodeManipulator confNodeManipulator;

    @Resource
    @Structure( type = RegistryNSNodeMapper.class )
    RegistryNSNodeManipulator namespaceNodeManipulator;

    @Resource
    @Structure( type = RegistryPropertiesMapper.class )
    RegistryPropertiesManipulator registryPropertiesManipulator;

    @Resource
    @Structure( type = RegistryTextValueMapper.class )
    RegistryTextValueManipulator registryTextValueManipulator;

    @Resource
    @Structure( type = RegistryNodeMetaMapper.class )
    RegistryNodeMetaManipulator confNodeMetaManipulator;

    @Resource
    @Structure( type = RegistryNSNodeMetaMapper.class )
    RegistryNSNodeMetaManipulator namespaceNodeMetaManipulator;

    @Resource
    @Structure( type = RegistryCommonDataMapper.class )
    RegistryCommonDataManipulator registryCommonDataManipulator;

    @Resource( type = RegistryMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator skeletonMasterManipulator;

    public RegistryMasterManipulatorImpl() {

    }

    public RegistryMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( RegistryMasterManipulatorImpl.class, Map.of(), this );
        this.skeletonMasterManipulator = new RegistryMasterTreeManipulatorImpl( driver );
    }


    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public RegistryNodeManipulator getRegistryNodeManipulator() {
        return this.confNodeManipulator;
    }

    @Override
    public RegistryNSNodeManipulator getNamespaceManipulator() {
        return this.namespaceNodeManipulator;
    }

    @Override
    public RegistryPropertiesManipulator getRegistryPropertiesManipulator() {
        return this.registryPropertiesManipulator;
    }

    @Override
    public RegistryTextValueManipulator getRegistryTextValueManipulator() {
        return this.registryTextValueManipulator;
    }

    @Override
    public RegistryNodeMetaManipulator getConfNodeMetaManipulator() {
        return this.confNodeMetaManipulator;
    }

    @Override
    public RegistryNSNodeMetaManipulator getNamespaceNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public RegistryCommonDataManipulator getRegistryCommonDataManipulator() {
        return this.registryCommonDataManipulator;
    }
}
