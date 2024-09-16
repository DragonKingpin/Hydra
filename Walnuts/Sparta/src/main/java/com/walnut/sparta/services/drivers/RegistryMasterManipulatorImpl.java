package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryCommonDataManipulator;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RegistryMasterManipulatorImpl implements RegistryMasterManipulator {
    @Resource
    RegistryNodeManipulator configNodeManipulator;

    @Resource
    RegistryNSNodeManipulator namespaceNodeManipulator;

    @Resource
    RegistryPropertiesManipulator registryPropertiesManipulator;

    @Resource
    RegistryTextValueManipulator registryTextValueManipulator;

    @Resource
    RegistryNodeMetaManipulator configNodeMetaManipulator;

    @Resource
    RegistryNSNodeMetaManipulator namespaceNodeMetaManipulator;

    @Resource
    RegistryCommonDataManipulator registryCommonDataManipulator;

    @Resource( type = RegistryMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator    skeletonMasterManipulator;


    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public RegistryNodeManipulator getRegistryNodeManipulator() {
        return this.configNodeManipulator;
    }

    @Override
    public RegistryNSNodeManipulator getNSNodeManipulator() {
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
    public RegistryNodeMetaManipulator getRegistryNodeMetaManipulator() {
        return this.configNodeMetaManipulator;
    }

    @Override
    public RegistryNSNodeMetaManipulator getNSNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public RegistryCommonDataManipulator getRegistryCommonDataManipulator() {
        return this.registryCommonDataManipulator;
    }
}
