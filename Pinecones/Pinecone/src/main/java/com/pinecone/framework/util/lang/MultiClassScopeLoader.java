package com.pinecone.framework.util.lang;

import com.pinecone.framework.util.name.Name;

import java.util.List;

public interface MultiClassScopeLoader extends ClassScopeLoader {
    List loads( Name name ) ;

    void addIncludeFilter( ClassFilter includeFilter ) ;

    void addExcludeFilter( ClassFilter excludeFilter ) ;

    void resetFilters    ( boolean useDefaultFilters );

}
