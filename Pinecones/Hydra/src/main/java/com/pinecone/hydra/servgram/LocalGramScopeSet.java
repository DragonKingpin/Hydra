package com.pinecone.hydra.servgram;

import com.pinecone.framework.unit.LinkedTreeSet;
import com.pinecone.framework.util.lang.ScopedPackage;

import java.util.Set;

public class LocalGramScopeSet extends ArchGramScopeSet {
    public LocalGramScopeSet( Set<ScopedPackage> scope, ClassLoader classLoader ) {
        super( scope, classLoader );
    }

    public LocalGramScopeSet( ClassLoader classLoader ) {
        super( new LinkedTreeSet<>(), classLoader );
    }

    public LocalGramScopeSet( GramFactory factory ) {
        super( new LinkedTreeSet<>(), factory.getClassLoader() );
    }
}
