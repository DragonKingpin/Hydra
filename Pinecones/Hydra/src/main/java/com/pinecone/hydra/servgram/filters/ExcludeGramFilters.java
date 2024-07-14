package com.pinecone.hydra.servgram.filters;

import com.pinecone.framework.util.lang.TypeFilter;
import com.pinecone.ulf.util.lang.HierarchyClassInspector;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

public class ExcludeGramFilters implements TypeFilter {
    protected HierarchyClassInspector  mClassInspector;

    public ExcludeGramFilters( HierarchyClassInspector inspector ) {
        this.mClassInspector = inspector;
    }

    @Override
    public boolean match( String szClassName, Object pool ) throws IOException {
        try{
            CtClass clz = ( (ClassPool) pool ).get( szClassName );
            if( clz.isInterface() ) {
                return true;
            }
            if( this.mClassInspector.isImplemented( clz, com.pinecone.hydra.servgram.Servgram.class ) ) {
                return false;
            }
            return !this.mClassInspector.hasOwnAnnotation( clz, com.pinecone.hydra.servgram.Gram.class ) ;
        }
        catch ( NotFoundException e ) {
            return true;
        }
    }
}
