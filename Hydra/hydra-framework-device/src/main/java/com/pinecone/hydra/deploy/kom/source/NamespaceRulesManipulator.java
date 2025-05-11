package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.GenericNamespaceRules;

public interface NamespaceRulesManipulator {
    void insert(GenericNamespaceRules classificationRules);

    void remove(GUID guid);

    GenericNamespaceRules getNamespaceRules(GUID guid);

    void update(GenericNamespaceRules classificationRules);
}
