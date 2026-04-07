package com.sauron.heist.heistron.orchestration;

import com.pinecone.framework.util.lang.TypeFilter;
import com.sauron.heist.heistron.Heistum;
import com.pinecone.ulf.util.lang.HierarchyClassInspector;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

public class ExcludeHeistletFilters implements TypeFilter {
    protected HierarchyClassInspector mClassInspector;

    public ExcludeHeistletFilters( HierarchyClassInspector inspector ) {
        this.mClassInspector = inspector;
    }

    @Override
    public boolean match( String szClassName, Object pool ) throws IOException {
        try{
            CtClass clz = ( (ClassPool) pool ).get( szClassName );
            if( clz.isInterface() ) {
                return true;
            }
            if( this.mClassInspector.isImplemented( clz, Heistum.class ) ) {
                return false;
            }
            return !this.mClassInspector.hasOwnAnnotation( clz, Heistlet.class ) ;
        }
        catch ( NotFoundException e ) {
            return true;
        }
    }
}