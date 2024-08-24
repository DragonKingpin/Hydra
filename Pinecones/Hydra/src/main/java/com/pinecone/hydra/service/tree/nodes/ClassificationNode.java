package com.pinecone.hydra.service.tree.nodes;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface ClassificationNode extends Pinenut {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    String getName();
    void setName(String name);

    GUID getRulesGUID();
    void setRulesGUID(GUID rulesGUID);

}
