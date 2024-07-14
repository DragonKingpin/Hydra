package com.pinecone.ulf.util.lang;

import com.pinecone.framework.util.lang.ClassScopeLoader;
import com.pinecone.framework.util.name.Name;

public interface TraitClassLoader extends ClassScopeLoader {
    @Override
    Class<? > load( Name simpleName ) throws ClassNotFoundException ;

    // Directly by it`s name.
    Class<? > loadByName( Name simpleName ) throws ClassNotFoundException ;

    // Scanning class`s annotations, methods or others.
    Class<? > loadInClassTrait( Name simpleName ) throws ClassNotFoundException ;

    TraitClassLoader updateScope();
}
