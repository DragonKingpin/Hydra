package com.pinecone.hydra.servgram;


import com.pinecone.framework.util.lang.ArchClassScopeSet;
import com.pinecone.framework.util.lang.ScopedPackage;

import java.util.Set;

public abstract class ArchGramScopeSet extends ArchClassScopeSet implements GramScope {
    protected ArchGramScopeSet( Set<ScopedPackage > scope, ClassLoader classLoader ) {
        super( scope, classLoader );
    }

}
