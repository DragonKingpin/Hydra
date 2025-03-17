package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.TaskElement;

public interface TaskMetaManipulator extends Pinenut {
    void insert( TaskElement serviceElement );

    void remove( GUID guid );

    void update( TaskElement serviceElement );

    TaskElement getTaskMeta( GUID guid );
}
