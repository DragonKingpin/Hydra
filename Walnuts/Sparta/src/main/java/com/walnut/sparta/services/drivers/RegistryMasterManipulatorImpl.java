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
    RegistryNodeManipulator confNodeManipulator;

    @Resource
    RegistryNSNodeManipulator namespaceNodeManipulator;

    @Resource
    RegistryPropertiesManipulator registryPropertiesManipulator;

    @Resource
    RegistryTextValueManipulator registryTextValueManipulator;

    @Resource
    RegistryNodeMetaManipulator confNodeMetaManipulator;

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
