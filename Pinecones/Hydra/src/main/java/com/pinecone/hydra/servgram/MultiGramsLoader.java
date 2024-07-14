package com.pinecone.hydra.servgram;

import com.pinecone.framework.util.lang.MultiClassScopeLoader;
import com.pinecone.framework.util.name.Name;
import com.pinecone.ulf.util.lang.MultiTraitClassLoader;

import java.util.List;

public interface MultiGramsLoader extends GramLoader, MultiClassScopeLoader, MultiTraitClassLoader {
    @Override
    List<Class<? > > loads( Name name ) ;

    @Override
    List<Class<? > > loadsByName( Name simpleName );

    @Override
    List<Class<? > > loadsInClassTrait( Name simpleName ) ;
}
