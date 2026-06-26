package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public interface InstanceLineageAdjacent extends Pinenut {
    GUID getInstanceGuid();
    void setInstanceGuid( GUID instanceGuid );

    GUID getParentInstanceGuid();
    void setParentInstanceGuid( GUID parentInstanceGuid );

    GUID getTaskGuid();
    void setTaskGuid( GUID taskGuid );

    GUID getParentTaskGuid();
    void setParentTaskGuid( GUID parentTaskGuid );

    String getTaskName();
    void setTaskName( String taskName );

    String getParentTaskName();
    void setParentTaskName( String parentTaskName );

    String getInstanceName();
    void setInstanceName( String instanceName );

    String getParentInstanceName();
    void setParentInstanceName( String parentInstanceName );

    LocalDateTime getBusinessTime();
    void setBusinessTime( LocalDateTime businessTime );

    LocalDateTime getParentBusinessTime();
    void setParentBusinessTime( LocalDateTime parentBusinessTime );
}
