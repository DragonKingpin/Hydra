package com.pinecone.hydra.registry.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface RegistryMasterManipulator extends KOIMasterManipulator {

    RegistryNodeManipulator       getRegistryNodeManipulator();

    RegistryNSNodeManipulator     getNSNodeManipulator();

    RegistryPropertiesManipulator getRegistryPropertiesManipulator();

    RegistryTextValueManipulator  getRegistryTextValueManipulator();

    RegistryNodeMetaManipulator   getRegistryNodeMetaManipulator();

    RegistryNSNodeMetaManipulator getNSNodeMetaManipulator();

    RegistryCommonDataManipulator getRegistryCommonDataManipulator();

}