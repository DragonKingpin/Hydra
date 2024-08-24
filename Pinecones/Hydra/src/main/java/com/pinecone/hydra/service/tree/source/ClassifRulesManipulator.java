package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericClassificationRules;

public interface ClassifRulesManipulator {
    //ClassifcationRules的CRUD
    void insertClassifRules(GenericClassificationRules classificationRules);
    void deleteClassifRules(GUID guid);
    GenericClassificationRules selectClassifRules(GUID guid);
    void updateClassifRules(GenericClassificationRules classificationRules);
}
