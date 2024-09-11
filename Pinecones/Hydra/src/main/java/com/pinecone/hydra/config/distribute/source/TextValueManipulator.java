package com.pinecone.hydra.config.distribute.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.config.distribute.entity.TextValue;

public interface TextValueManipulator extends Pinenut {
    void insert(TextValue textValue);

    void remove(GUID guid);

    TextValue getTextValue(GUID guid);

    void update(TextValue textValue);
}
