package com.pinecone.ulf.util.lang;

import com.pinecone.framework.util.lang.MultiClassScopeLoader;
import com.pinecone.framework.util.name.Name;

import java.util.List;

public interface MultiTraitClassLoader extends TraitClassLoader, MultiClassScopeLoader {
    List<Class<? > > loads( Name name ) ;

    List<Class<? > > loadsByName( Name simpleName );

    List<Class<? > > loadsInClassTrait( Name simpleName ) ;
}
