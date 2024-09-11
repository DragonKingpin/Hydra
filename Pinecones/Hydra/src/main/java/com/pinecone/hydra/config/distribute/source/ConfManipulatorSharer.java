package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ConfManipulatorSharer extends Pinenut {
    ConfNodeManipulator             getConfigurationManipulator();

    NamespaceNodeManipulator        getNamespaceManipulator();

    PropertiesManipulator           getPropertiesManipulator();

    TextValueManipulator            getTextValueManipulator();

    ConfTreeManipulator             getConfTreeManipulator();

    ConfNodeOwnerManipulator        getConfNodeOwnerManipulator();

    ConfNodePathManipulator         getConfNodePathManipulator();

    ConfNodeMetaManipulator         getConfNodeMetaManipulator();

    NamespaceNodeMetaManipulator    getNamespaceNodeMetaManipulator();

    NodeCommonDataManipulator       getNodeCommonDataManipulator();
}
