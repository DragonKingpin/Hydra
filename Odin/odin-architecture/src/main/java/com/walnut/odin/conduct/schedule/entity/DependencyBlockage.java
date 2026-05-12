package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface DependencyBlockage extends Pinenut {

    GUID getInstanceGuid();

    void setInstanceGuid( GUID instanceGuid );

    GUID getDependentInstanceGuid();

    void setDependentInstanceGuid( GUID dependentInstanceGuid );

}
