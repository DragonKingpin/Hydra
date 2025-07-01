package com.walnut.odin.task.troll;

import java.net.URI;

import com.pinecone.hydra.task.ArchTaskInstance;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.task.RavenTaskInstance;

public abstract class ArchRavenTaskInstance extends ArchTaskInstance implements RavenTaskInstance {

    protected URI processImageURI;

    public ArchRavenTaskInstance( InstanceEntry instanceEntry ) {
        super( instanceEntry );
    }

    @Override
    public URI getProcessImageURI() {
        return this.processImageURI;
    }

}
