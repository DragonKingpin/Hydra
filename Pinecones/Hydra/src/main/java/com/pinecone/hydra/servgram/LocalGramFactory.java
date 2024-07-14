package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.executum.TaskManager;

public class LocalGramFactory extends ArchGramFactory {
    public LocalGramFactory( TaskManager taskManager, ClassLoader classLoader, MultiGramsLoader gramLoader, GramScope gramScope ) {
        super( taskManager, classLoader, gramLoader, gramScope );
    }

    public LocalGramFactory( TaskManager taskManager ) {
        this( taskManager, taskManager.getClassLoader(), null, null );

        this.mClassScope        = new LocalGramScopeSet( this );
        this.mTraitClassLoader  = new LocalGramLoader( this );
    }

    public LocalGramFactory( TaskManager taskManager, GramScope gramScope ) {
        this( taskManager, taskManager.getClassLoader(), null, gramScope );

        this.mTraitClassLoader = new LocalGramLoader( this );
    }
}
