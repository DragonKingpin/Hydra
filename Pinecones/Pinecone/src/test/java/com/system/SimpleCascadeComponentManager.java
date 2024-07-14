package com.system;

import com.pinecone.framework.system.architecture.ArchCascadeComponent;
import com.pinecone.framework.system.architecture.ArchCascadeComponentManager;
import com.pinecone.framework.system.architecture.CascadeComponent;
import com.pinecone.framework.system.architecture.CascadeComponentManager;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.framework.util.name.UniNamespace;

class SimpleCascadeComponent extends ArchCascadeComponent {
    public SimpleCascadeComponent( Namespace name, CascadeComponentManager manager, CascadeComponent parent ) {
        super( name, manager, parent );
    }

    public SimpleCascadeComponent( String name, CascadeComponentManager manager ) {
        super( new UniNamespace( name ), manager, null );
    }
}

public class SimpleCascadeComponentManager extends ArchCascadeComponentManager {
}
