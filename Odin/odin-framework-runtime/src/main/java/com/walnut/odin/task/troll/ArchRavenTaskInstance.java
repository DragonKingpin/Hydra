package com.walnut.odin.task.troll;

import java.net.URI;
import java.net.URISyntaxException;

import com.pinecone.hydra.task.ArchTaskInstance;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.task.RavenTaskInstance;

public abstract class ArchRavenTaskInstance extends ArchTaskInstance implements RavenTaskInstance {

    protected URI processImageURI;

    public ArchRavenTaskInstance( InstanceEntry instanceEntry ) {
        super( instanceEntry );

        try {
            TaskElement taskElement = this.getInstanceEntry().taskElement();
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
    }

    @Override
    public URI getProcessImageURI() {
        return this.processImageURI;
    }

}
