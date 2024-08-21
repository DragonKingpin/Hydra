package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericClassificationRules;

public interface ClassifRulesManipinate {
    //ClassifcationRules的CRUD
    void saveClassifRules(GenericClassificationRules classificationRules);
    void deleteClassifRules(GUID UUID);
    GenericClassificationRules selectClassifRules(GUID UUID);
    void updateClassifRules(GenericClassificationRules classificationRules);
}
