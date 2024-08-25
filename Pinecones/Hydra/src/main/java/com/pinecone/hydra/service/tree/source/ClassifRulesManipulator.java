package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericClassificationRules;

public interface ClassifRulesManipulator {
    //ClassifcationRules的CRUD
    void insert(GenericClassificationRules classificationRules);
    void delete(GUID guid);
    GenericClassificationRules getClassifRules(GUID guid);
    void update(GenericClassificationRules classificationRules);
}
