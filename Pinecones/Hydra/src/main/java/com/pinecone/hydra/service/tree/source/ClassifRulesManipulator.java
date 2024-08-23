package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericClassificationRules;

public interface ClassifRulesManipulator {
    //ClassifcationRules的CRUD
    void saveClassifRules(GenericClassificationRules classificationRules);
    void deleteClassifRules(GUID UUID);
    GenericClassificationRules selectClassifRules(GUID UUID);
    void updateClassifRules(GenericClassificationRules classificationRules);
}
