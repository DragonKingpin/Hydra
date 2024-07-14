package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.unit.LinkedTreeSet;
import com.pinecone.framework.util.lang.ArchClassScopeSet;
import com.pinecone.framework.util.lang.ScopedPackage;

import java.util.Set;

public class LocalRaiderScopeSet extends ArchClassScopeSet {
    public LocalRaiderScopeSet( Set<ScopedPackage > scope, ClassLoader classLoader ) {
        super( scope, classLoader );
    }

    public LocalRaiderScopeSet( ClassLoader classLoader ) {
        super( new LinkedTreeSet<>(), classLoader );
    }

    public LocalRaiderScopeSet( RaiderFactory factory ) {
        super( new LinkedTreeSet<>(), factory.getClassLoader() );
    }
}
