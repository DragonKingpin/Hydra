package com.pinecone.hydra.registry.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface RegistryMasterManipulator extends KOIMasterManipulator {

    RegistryConfigNodeManipulator getConfigNodeManipulator();

    RegistryNSNodeManipulator     getNSNodeManipulator();

    RegistryPropertiesManipulator getPropertiesManipulator();

    RegistryTextFileManipulator   getTextFileManipulator();

    RegistryNodeMetaManipulator   getNodeMetaManipulator();

    RegistryNSNodeMetaManipulator getNSNodeMetaManipulator();

    RegistryAttributesManipulator getAttributesManipulator();

}
