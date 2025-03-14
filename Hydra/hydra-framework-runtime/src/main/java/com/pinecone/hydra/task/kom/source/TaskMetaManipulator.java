package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.TaskElement;

public interface TaskMetaManipulator {
    void insert( TaskElement taskElement );

    void remove( GUID guid );

    void update( TaskElement taskElement );

    TaskElement getTaskMeta( GUID guid );
}
