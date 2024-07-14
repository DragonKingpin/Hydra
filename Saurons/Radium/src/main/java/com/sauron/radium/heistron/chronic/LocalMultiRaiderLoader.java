package com.sauron.radium.heistron.chronic;

import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.name.Name;
import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import com.pinecone.ulf.util.lang.ArchMultiScopeLoader;
import com.pinecone.ulf.util.lang.GenericPreloadClassInspector;
import com.pinecone.ulf.util.lang.PooledClassCandidateScanner;
import javassist.ClassPool;
import javassist.bytecode.annotation.Annotation;

public class LocalMultiRaiderLoader extends ArchMultiScopeLoader implements  MultiRaiderLoader {
    protected AnnotationValueFilter mAnnoValueFilter    ;

    protected LocalMultiRaiderLoader( ClassScope classScope, ClassLoader classLoader, ClassPool classPool ) {
        super( classScope, classLoader, classPool, null, null );

        this.mClassScanner         = new PooledClassCandidateScanner( new LocalRaiderScopeSet( this.mClassLoader ), this.mClassLoader, this.mClassPool );
        this.mClassInspector       = new GenericPreloadClassInspector( this.mClassPool );
        this.mClassScanner.addExcludeFilter( new ExcludeRaiderletFilters( this.mClassInspector ) );
        this.setAnnotationValueFilter( new RaiderletAnnotationValueFilter() );
    }

    protected LocalMultiRaiderLoader( ClassScope classScope, ClassLoader classLoader ) {
        this( classScope, classLoader, ClassPool.getDefault() );
    }

    public LocalMultiRaiderLoader( RaiderFactory factory ) {
        this( factory.getClassScope(), factory.getClassLoader() );
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
    public Class<? extends Raider > load( Name simpleName ) throws ClassNotFoundException {
        return (Class<? extends Raider > )super.load( simpleName );
    }

    // Directly by it`s name.
    @Override
    @SuppressWarnings( "unchecked" )
    public Class<? extends Raider > loadByName( Name simpleName ) throws ClassNotFoundException {
        return (Class<? extends Raider > )super.loadByName( simpleName );
    }

    // Scanning class`s annotations, methods or others.
    @Override
    @SuppressWarnings( "unchecked" )
    public Class<? extends Raider > loadInClassTrait( Name simpleName ) throws ClassNotFoundException {
        return (Class<? extends Raider > )super.loadInClassTrait( simpleName );
    }

    @Override
    protected Class<? extends Raider > loadSingleByFullClassName( String szFullClassName ) {
        try {
            Class<?> clazz = this.mClassLoader.loadClass( szFullClassName );
            if( this.filter( clazz ) ) {
                return null;
            }
            if ( Raider.class.isAssignableFrom( clazz ) ) {
                return clazz.asSubclass( Raider.class );
            }
        }
        catch ( ClassNotFoundException e ) {
            return null;
        }

        return null;
    }

    @Override
    public MultiRaiderLoader updateScope() {
        return (MultiRaiderLoader)super.updateScope();
    }

    @Override
    public void clearCache() {
        this.mLoadedClassesPool.clear();
        this.mVisitedClasses.clear();
    }

}
