package com.pinecone.ulf.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;

public interface PreloadClassInspector extends Pinenut {
    CtClass preloadClass( String szClassName ) throws NotFoundException ;

    default boolean hasOwnAnnotation( String szClassName, Class<?> interf ) throws NotFoundException {
        return this.hasOwnAnnotation( this.preloadClass( szClassName ), interf );
    }

    Annotation[] queryVisibleAnnotations( CtClass clazz );

    boolean hasOwnAnnotation( CtClass clazz, Class<?> annotationClass ) ;

    default boolean hasOwnAnnotations( String szClassName, Class<?>[] annotationClasses ) throws NotFoundException {
        return this.hasOwnAnnotations( this.preloadClass( szClassName ), annotationClasses );
    }

    boolean hasOwnAnnotations( CtClass clazz, Class<?>[] annotationClasses );

    default boolean hasOwnMethod( String szClassName, String methodName ) throws NotFoundException {
        return this.hasOwnMethod(this.preloadClass(szClassName), methodName);
    }

    boolean hasOwnMethod( CtClass clazz, String methodName ) ;

    default boolean hasOwnField( String szClassName, String fieldName ) throws NotFoundException {
        return this.hasOwnField( this.preloadClass(szClassName), fieldName );
    }

    boolean hasOwnField( CtClass clazz, String fieldName ) ;

    default boolean hasOwnMethods( String szClassName, String[] methodNames ) throws NotFoundException {
        return this.hasOwnMethods( this.preloadClass(szClassName), methodNames );
    }

    boolean hasOwnMethods( CtClass clazz, String[] methodNames ) ;

    default boolean hasOwnFields( String szClassName, String[] fieldNames ) throws NotFoundException {
        return this.hasOwnFields( this.preloadClass(szClassName), fieldNames );
    }

    boolean hasOwnFields( CtClass clazz, String[] fieldNames ) ;
}
