package com.pinecone.ulf.util.lang;

import com.pinecone.framework.util.lang.TypeFilter;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;

public class SimpleAnnotationExcludeFilter implements TypeFilter {
    protected HierarchyClassInspector mClassInspector;

    protected Class<? >               mAnnotationType;

    public SimpleAnnotationExcludeFilter( HierarchyClassInspector inspector, Class<? > annotationType ) {
        this.mClassInspector = inspector;
        this.mAnnotationType = annotationType;
    }

    @Override
    public boolean match( String szClassName, Object pool ) throws IOException {
        try{
            CtClass clz = ( (ClassPool) pool ).get( szClassName );
            return !this.mClassInspector.hasOwnAnnotation( clz, this.mAnnotationType ) ;
        }
        catch ( NotFoundException e ) {
            return true;
        }
    }
}