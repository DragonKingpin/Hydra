package com.walnut.odin.task.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskFamilyMeta;
import com.walnut.odin.task.entity.RavenTaskMeta;

public interface TaskExMetaManipulator extends Pinenut {

    void insert( RavenTaskMeta taskMeta );

    void remove( GUID UUID );

    RavenTaskMeta getTaskExMeta( GUID guid, TaskFamilyMeta kernelMeta );

    void update( RavenTaskMeta taskMeta );

}
