package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface InstanceAtlasNode extends Pinenut {
    GUID getGuid();
    void setGuid(GUID guid);

    GUID getInstanceGuid();
    void setInstanceGuid(GUID instanceGuid);

    String getNodeName();
    void setNodeName(String nodeName);

    boolean isSource();
    void setSource( boolean source );
}
