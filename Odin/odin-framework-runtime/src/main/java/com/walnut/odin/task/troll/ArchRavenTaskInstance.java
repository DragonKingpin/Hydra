package com.walnut.odin.task.troll;

import java.net.URI;
import java.net.URISyntaxException;

import com.pinecone.hydra.task.ArchTaskInstance;
import com.pinecone.hydra.task.Task;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.task.RavenTaskInstance;

public abstract class ArchRavenTaskInstance extends ArchTaskInstance implements RavenTaskInstance {

    protected URI processImageURI;

    protected InstanceInstrument instanceInstrument;

    public ArchRavenTaskInstance( InstanceEntry instanceEntry, Task ownedTask ) {
        super( instanceEntry, ownedTask );

        try {
            TaskElement taskElement = ownedTask.getTaskElement();
            if ( taskElement != null ) {
                String imagePath = taskElement.getImagePath();
                if ( imagePath != null ) {
                    this.processImageURI = URI.create( imagePath );
                }
            }
        }
        catch ( IllegalArgumentException e ) {
            this.processImageURI = null;
        }

        this.instanceInstrument = instanceEntry.getTaskInstrument().getInstanceInstrument();
    }

    @Override
    public URI getProcessImageURI() {
        return this.processImageURI;
    }

    @Override
    public InstanceInstrument instanceInstrument() {
        return this.instanceInstrument;
    }
}
