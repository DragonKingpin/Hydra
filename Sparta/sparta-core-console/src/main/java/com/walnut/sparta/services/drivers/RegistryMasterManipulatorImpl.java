package com.walnut.sparta.services.drivers;

import com.pinecone.hydra.registry.source.RegistryMasterManipulator;
import com.pinecone.hydra.registry.source.RegistryConfigNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeManipulator;
import com.pinecone.hydra.registry.source.RegistryNSNodeMetaManipulator;
import com.pinecone.hydra.registry.source.RegistryAttributesManipulator;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;
import com.pinecone.hydra.registry.source.RegistryTextFileManipulator;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RegistryMasterManipulatorImpl implements RegistryMasterManipulator {
    @Resource
    RegistryConfigNodeManipulator configNodeManipulator;

    @Resource
    RegistryNSNodeManipulator namespaceNodeManipulator;

    @Resource
    RegistryPropertiesManipulator registryPropertiesManipulator;

    @Resource
    RegistryTextFileManipulator registryTextFileManipulator;

    @Resource
    RegistryNodeMetaManipulator configNodeMetaManipulator;

    @Resource
    RegistryNSNodeMetaManipulator namespaceNodeMetaManipulator;

    @Resource
    RegistryAttributesManipulator registryAttributesManipulator;

    @Resource( type = RegistryMasterTreeManipulatorImpl.class )
    KOISkeletonMasterManipulator    skeletonMasterManipulator;


    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this.skeletonMasterManipulator;
    }

    @Override
    public RegistryConfigNodeManipulator getConfigNodeManipulator() {
        return this.configNodeManipulator;
    }

    @Override
    public RegistryNSNodeManipulator getNSNodeManipulator() {
        return this.namespaceNodeManipulator;
    }

    @Override
    public RegistryPropertiesManipulator getPropertiesManipulator() {
        return this.registryPropertiesManipulator;
    }

    @Override
    public RegistryTextFileManipulator getTextFileManipulator() {
        return this.registryTextFileManipulator;
    }

    @Override
    public RegistryNodeMetaManipulator getNodeMetaManipulator() {
        return this.configNodeMetaManipulator;
    }

    @Override
    public RegistryNSNodeMetaManipulator getNSNodeMetaManipulator() {
        return this.namespaceNodeMetaManipulator;
    }

    @Override
    public RegistryAttributesManipulator getAttributesManipulator() {
        return this.registryAttributesManipulator;
    }
}
