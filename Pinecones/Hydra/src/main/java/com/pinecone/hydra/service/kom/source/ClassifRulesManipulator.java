package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericClassificationRules;

public interface ClassifRulesManipulator {
    //ClassifcationRules的CRUD
    void insert(GenericClassificationRules classificationRules);
    void remove(GUID guid);
    GenericClassificationRules getClassifRules(GUID guid);
    void update(GenericClassificationRules classificationRules);
}
