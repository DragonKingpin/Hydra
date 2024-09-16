package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.TextValue;

public interface RegistryTextValueManipulator extends Pinenut {
    void insert(TextValue textValue);

    void remove(GUID guid);

    TextValue getTextValue(GUID guid);

    void update(TextValue textValue);
}
