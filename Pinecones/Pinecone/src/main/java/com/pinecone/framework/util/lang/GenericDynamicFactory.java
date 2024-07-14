package com.pinecone.framework.util.lang;


public class GenericDynamicFactory extends ArchDynamicFactory {
    public GenericDynamicFactory( ClassLoader classLoader, ClassScope classScope ) {
        super( classLoader, classScope );
    }

    public GenericDynamicFactory( ClassLoader classLoader ) {
        this( classLoader, new GenericClassScopeSet( classLoader ) );
    }

    public GenericDynamicFactory() {
        this( Thread.currentThread().getContextClassLoader() );
    }
}
