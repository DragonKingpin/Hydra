package com.sauron.radium.heistron.orchestration;


import com.pinecone.framework.system.executum.TaskManager;
import com.pinecone.hydra.servgram.ArchGramFactory;
import com.pinecone.hydra.servgram.GramScope;
import com.pinecone.hydra.servgram.LocalGramScopeSet;
import com.pinecone.hydra.servgram.MultiGramsLoader;


public class LocalHeistletFactory extends ArchGramFactory {
    public LocalHeistletFactory( TaskManager taskManager, ClassLoader classLoader, MultiGramsLoader gramLoader, GramScope gramScope ) {
        super( taskManager, classLoader, gramLoader, gramScope );
    }

    public LocalHeistletFactory( TaskManager taskManager ) {
        this( taskManager, taskManager.getClassLoader(), null, null );

        this.mClassScope        = new LocalGramScopeSet( this );
        this.mTraitClassLoader  = new LocalHeistletLoader( this );
    }

    public LocalHeistletFactory( TaskManager taskManager, GramScope gramScope ) {
        this( taskManager, taskManager.getClassLoader(), null, gramScope );

        this.mTraitClassLoader = new LocalHeistletLoader( this );
    }
}
