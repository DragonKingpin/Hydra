package com.pinecone.hydra.servgram;

import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import com.pinecone.ulf.util.lang.TraitClassLoader;

public interface GramLoader extends TraitClassLoader {
    @Override
    Class<? extends Servgram > load( Name simpleName ) throws ClassNotFoundException ;

    // Directly by it`s name.
    @Override
    Class<? extends Servgram > loadByName( Name simpleName ) throws ClassNotFoundException ;

    // Scanning class`s annotations, methods or others.
    @Override
    Class<? extends Servgram > loadInClassTrait( Name simpleName ) throws ClassNotFoundException ;

    @Override
    GramLoader updateScope();

    void setAnnotationValueFilter( AnnotationValueFilter filter );
}
