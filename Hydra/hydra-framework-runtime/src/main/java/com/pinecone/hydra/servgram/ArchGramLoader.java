package com.pinecone.hydra.servgram;

import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import com.pinecone.ulf.util.lang.*;
import javassist.ClassPool;
import javassist.bytecode.annotation.Annotation;

/**
 * Notice: TODO, IOC Inject Implement.
 */
public abstract class ArchGramLoader extends ArchMultiScopeLoader implements MultiGramsLoader {
    protected AnnotationValueFilter                mAnnoValueFilter    ;

    protected ArchGramLoader( GramScope gramScope, ClassLoader classLoader, ClassPool classPool ) {
        super( gramScope, classLoader, classPool, null, null );

        this.mClassScanner         = new PooledClassCandidateScanner( new LocalGramScopeSet( this.mClassLoader ), this.mClassLoader, this.mClassPool );
        this.mClassInspector       = new GenericPreloadClassInspector( this.mClassPool );
    }

    protected ArchGramLoader( GramScope gramScope, ClassLoader classLoader ) {
        this( gramScope, classLoader, ClassPool.getDefault() );
    }

    @Override
    public void setAnnotationValueFilter( AnnotationValueFilter filter ) {
        this.mAnnoValueFilter = filter;
    }

    @Override
    protected boolean isAnnotationQualified( Annotation that, String szName ) {
        return !this.mAnnoValueFilter.match( that, szName );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Class<? extends Servgram > load( Name simpleName ) throws ClassNotFoundException {
        return (Class<? extends Servgram > )super.load( simpleName );
    }

    // Directly by it`s name.
    @Override
    @SuppressWarnings( "unchecked" )
    public Class<? extends Servgram > loadByName( Name simpleName ) throws ClassNotFoundException {
        return (Class<? extends Servgram > )super.loadByName( simpleName );
    }

    // Scanning class`s annotations, methods or others.
    @Override
    @SuppressWarnings( "unchecked" )
    public Class<? extends Servgram > loadInClassTrait( Name simpleName ) throws ClassNotFoundException {
        return (Class<? extends Servgram > )super.loadInClassTrait( simpleName );
    }

    @Override
    protected Class<? extends Servgram > loadSingleByFullClassName( String szFullClassName ) {
        try {
            Class<?> clazz = this.mClassLoader.loadClass( szFullClassName );
            if( this.filter( clazz ) ) {
                return null;
            }
            if ( Servgram.class.isAssignableFrom( clazz ) ) {
                return clazz.asSubclass( Servgram.class );
            }
        }
        catch ( ClassNotFoundException e ) {
            return null;
        }

        return null;
    }

    @Override
    public MultiGramsLoader updateScope() {
        return (MultiGramsLoader)super.updateScope();
    }

    @Override
    public void clearCache() {
        this.mLoadedClassesPool.clear();
        this.mVisitedClasses.clear();
    }
}
