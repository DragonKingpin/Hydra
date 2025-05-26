package com.pinecone.hydra.proc.image;

import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrogen;

public class UniformImageLoader extends ArchImageLoader implements ImageLoader {

    public UniformImageLoader( Namespace name, Hydrogen system, HyComponent parent, ClassLoader classLoader ) {
        super( name, system, parent, classLoader );
    }

    public UniformImageLoader( Hydrogen system, HyComponent parent ) {
        this( null, system, parent, system.getGlobalClassLoader() );
    }

    public UniformImageLoader( Hydrogen system ) {
        this( system, null );
    }

}
