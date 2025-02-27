package com.pinecone.hydra.servgram;

import com.pinecone.hydra.servgram.filters.ExcludeGramFilters;
import com.pinecone.hydra.servgram.filters.GramAnnotationValueFilter;

public class LocalGramLoader extends ArchGramLoader {
    public LocalGramLoader( GramScope gramScope, ClassLoader classLoader ) {
        super( gramScope, classLoader );

        this.mClassScanner.addExcludeFilter( new ExcludeGramFilters( this.mClassInspector ) );
        this.setAnnotationValueFilter( new GramAnnotationValueFilter() );
    }

    public LocalGramLoader( GramFactory factory ) {
        this( factory.getClassScope(), factory.getClassLoader() );
    }
}
