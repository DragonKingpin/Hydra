package com.pinecone.hydra.proc.image;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public abstract class ArchImageLoader extends ArchSystemCascadeComponent implements ImageLoader {
    protected ClassLoader         mClassLoader;

    public ArchImageLoader( Namespace name, Hydrogen system, HyComponent parent, ClassLoader classLoader ) {
        super( name, system, system.getComponentManager(), parent );

        this.mClassLoader = classLoader;
    }

    public ArchImageLoader( Hydrogen system, HyComponent parent ) {
        this( null, system, parent, system.getGlobalClassLoader() );
    }

    public ArchImageLoader( Hydrogen system ) {
        this( system, null );
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

}
