package com.pinecone.framework.util.lang;

import java.util.LinkedHashSet;
import java.util.Set;

public class GenericClassScopeSet extends ArchClassScopeSet {
    public GenericClassScopeSet( Set<ScopedPackage > scope, ClassLoader classLoader ) {
        super( scope, classLoader );
    }

    public GenericClassScopeSet( ClassLoader classLoader ) {
        this( new LinkedHashSet<>(), classLoader );
    }
}
