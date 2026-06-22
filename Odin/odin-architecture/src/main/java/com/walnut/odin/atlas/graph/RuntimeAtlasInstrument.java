package com.walnut.odin.atlas.graph;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;

/**
 * Odin runtime atlas facade.
 *
 * The current atlas kernel is task-lineage based: guid means task guid, and
 * parent guid means upstream task guid.
 */
public interface RuntimeAtlasInstrument extends Pinenut {

    TaskInstrument taskInstrument();

    void addDependency( GUID taskGuid, GUID parentTaskGuid );

    void removeDependency( GUID taskGuid, GUID parentTaskGuid );

    List<GUID> fetchParentTaskGuids( GUID taskGuid );

    List<GUID> fetchChildTaskGuids( GUID taskGuid );

    List<TaskElement> fetchParentTasks( GUID taskGuid );

    List<TaskElement> fetchChildTasks( GUID taskGuid );

    TaskElement queryTaskElementByGuid( GUID taskGuid );

    long countParents( GUID taskGuid );

    long countChildren( GUID taskGuid );

}
