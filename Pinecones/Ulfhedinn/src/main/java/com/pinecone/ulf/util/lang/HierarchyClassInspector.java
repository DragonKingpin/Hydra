package com.pinecone.ulf.util.lang;

import javassist.CtClass;
import javassist.NotFoundException;

public interface HierarchyClassInspector extends PreloadClassInspector {

    default boolean isImplementedDirectly( String szClassName, Class<?> interf ) throws NotFoundException {
        return this.isImplementedDirectly( this.preloadClass( szClassName ), interf );
    }

    boolean isImplementedDirectly(CtClass clazz, Class<?> interf ) throws NotFoundException ;

    default boolean isImplemented( String szClassName, Class<?> interf ) throws NotFoundException {
        return this.isImplemented( this.preloadClass( szClassName ), interf );
    }

    boolean isImplemented( CtClass clazz, Class<?> interf ) throws NotFoundException ;

    default boolean isExtendedDirectly( String szClassName, Class<?> interf ) throws NotFoundException {
        return this.isExtendedDirectly( this.preloadClass( szClassName ), interf );
    }

    boolean isExtendedDirectly( CtClass clazz, Class<?> parent ) throws NotFoundException ;

    default boolean isExtended( String szClassName, Class<?> interf ) throws NotFoundException {
        return this.isExtended( this.preloadClass( szClassName ), interf );
    }

    boolean isExtended( CtClass clazz, Class<?> parent ) throws NotFoundException ;

}
