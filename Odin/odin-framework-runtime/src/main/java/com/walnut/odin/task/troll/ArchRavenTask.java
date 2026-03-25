package com.walnut.odin.task.troll;

import java.util.Map;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.ArchTask;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.walnut.odin.task.RavenTask;


public abstract class ArchRavenTask extends ArchTask implements RavenTask {

    public ArchRavenTask( Identification serviceId, TaskElement serviceElement, Map<String, Object > metaDataScope ){
        super( serviceId, serviceElement, metaDataScope );
    }

    public ArchRavenTask( Identification serviceId, TaskElement serviceElement ){
        this( serviceId, serviceElement, null );
    }

    @Override
    public TaskElement getTaskElement() {
        return  super.getTaskElement();
    }

}
