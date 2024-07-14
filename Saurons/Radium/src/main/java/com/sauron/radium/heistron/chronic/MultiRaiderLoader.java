package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.util.lang.MultiClassScopeLoader;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import com.pinecone.ulf.util.lang.MultiTraitClassLoader;

import java.util.List;

public interface MultiRaiderLoader extends MultiClassScopeLoader, MultiTraitClassLoader {
    @Override
    Class<? extends Raider > load( Name simpleName ) throws ClassNotFoundException ;

    // Directly by it`s name.
    @Override
    Class<? extends Raider > loadByName( Name simpleName ) throws ClassNotFoundException ;

    // Scanning class`s annotations, methods or others.
    @Override
    Class<? extends Raider > loadInClassTrait( Name simpleName ) throws ClassNotFoundException ;

    @Override
    MultiRaiderLoader updateScope();

    void setAnnotationValueFilter( AnnotationValueFilter filter );

    @Override
    List<Class<? > > loads( Name name ) ;

    @Override
    List<Class<? > > loadsByName( Name simpleName );

    @Override
    List<Class<? > > loadsInClassTrait( Name simpleName ) ;
}
