package com.walnut.odin.task.troll;

import java.net.URI;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.ArchTask;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.walnut.odin.task.RavenTask;
import com.walnut.odin.task.entity.RavenTaskElement;
import com.walnut.odin.task.entity.RavenTaskMeta;

public abstract class ArchRavenTask extends ArchTask implements RavenTask {

    public ArchRavenTask( Identification serviceId, TaskElement serviceElement, Map<String, Object > metaDataScope ){
        super( serviceId, serviceElement, metaDataScope );
    }

    public ArchRavenTask( Identification serviceId, TaskElement serviceElement ){
        this( serviceId, serviceElement, null );
    }

    @Override
    public RavenTaskElement getTaskElement() {
        return (RavenTaskElement) super.getTaskElement();
    }

    @Override
    public RavenTaskMeta getExtraMeta() {
        return this.getTaskElement().getExtraMeta();
    }

    @Override
    public GUID getDeploySchemeId() {
        return this.getExtraMeta().getDeploySchemeId();
    }
}
