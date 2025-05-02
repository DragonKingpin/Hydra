package com.walnut.odin.task.entity;

import com.pinecone.hydra.task.kom.entity.TaskElement;

public interface RavenTaskElement extends TaskElement {

    @Override
    RavenTaskMeta getExtraMeta();

    void setExtraMeta( RavenTaskMeta meta );

    RavenTaskElement from( TaskElement taskElement );


}
