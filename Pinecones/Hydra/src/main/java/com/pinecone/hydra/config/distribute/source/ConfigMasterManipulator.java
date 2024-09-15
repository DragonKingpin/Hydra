package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ConfigMasterManipulator extends Pinenut {
    ConfigNodeManipulator           getConfigurationManipulator();

    ConfigNSNodeManipulator         getNamespaceManipulator();

    PropertiesManipulator           getPropertiesManipulator();

    TextValueManipulator            getTextValueManipulator();

    ConfigNodeMetaManipulator       getConfNodeMetaManipulator();

    ConfigNSNodeMetaManipulator     getNamespaceNodeMetaManipulator();

    NodeCommonDataManipulator       getNodeCommonDataManipulator();
}
