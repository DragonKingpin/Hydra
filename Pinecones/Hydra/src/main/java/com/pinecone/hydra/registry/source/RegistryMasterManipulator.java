package com.pinecone.hydra.registry.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface RegistryMasterManipulator extends KOIMasterManipulator {

    RegistryNodeManipulator getRegistryNodeManipulator();

    RegistryNSNodeManipulator getNamespaceManipulator();

    RegistryPropertiesManipulator getRegistryPropertiesManipulator();

    RegistryTextValueManipulator getRegistryTextValueManipulator();

    RegistryNodeMetaManipulator getConfNodeMetaManipulator();

    RegistryNSNodeMetaManipulator getNamespaceNodeMetaManipulator();

    RegistryCommonDataManipulator getRegistryCommonDataManipulator();

}
