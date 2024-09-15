package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ConfManipulatorSharer extends Pinenut {
    ConfNodeManipulator             getConfigurationManipulator();

    NamespaceNodeManipulator        getNamespaceManipulator();

    PropertiesManipulator           getPropertiesManipulator();

    TextValueManipulator            getTextValueManipulator();

    ConfNodeMetaManipulator         getConfNodeMetaManipulator();

    NamespaceNodeMetaManipulator    getNamespaceNodeMetaManipulator();

    NodeCommonDataManipulator       getNodeCommonDataManipulator();
}
