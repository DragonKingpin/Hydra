package com.pinecone.framework.util.lang;

public class ClassScopeNSProtocolIteratorsFactory extends GenericScopeNSProtocolIteratorsFactory {

    public ClassScopeNSProtocolIteratorsFactory( ClassLoader classLoader, ClassScope searchScope ) {
        super( classLoader, searchScope, ".class" );
    }

}
