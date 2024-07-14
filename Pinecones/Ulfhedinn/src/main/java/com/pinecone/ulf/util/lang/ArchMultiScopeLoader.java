package com.pinecone.ulf.util.lang;
import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.unit.LinkedTreeSet;
import com.pinecone.framework.util.lang.ArchClassScopeLoader;
import com.pinecone.framework.util.lang.ClassScanner;
import com.pinecone.framework.util.lang.ClassScope;
import com.pinecone.framework.util.lang.ScopedPackage;
import com.pinecone.framework.util.name.MultiScopeName;
import com.pinecone.framework.util.name.Name;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ArchMultiScopeLoader extends ArchClassScopeLoader implements MultiTraitClassLoader {
    protected ClassScanner                         mClassScanner       ;
    protected ClassPool                            mClassPool          ;
    protected Map<String, CtClass >                mLoadedClassesPool  ;
    protected Set<String >                         mVisitedClasses     ;
    protected HierarchyClassInspector              mClassInspector     ;

    protected ArchMultiScopeLoader( ClassScope classScope, ClassLoader classLoader, ClassPool classPool, ClassScanner classScanner, HierarchyClassInspector classInspector ) {
        super( classScope, classLoader );

        this.mClassPool            = classPool;
        this.mLoadedClassesPool    = new LinkedTreeMap<>();
        this.mVisitedClasses       = new LinkedTreeSet<>();
        this.mClassScanner         = classScanner;
        this.mClassInspector       = classInspector;
    }

    @Override
    public Class<? > load( Name simpleName ) throws ClassNotFoundException {
        try{
            Class<? > c = this.loadByName( simpleName );
            if( c != null ) {
                return c;
            }
        }
        catch ( ClassNotFoundException e ) {
            this.handleIgnoreException( e );
        }

        return this.loadInClassTrait( simpleName );
    }

    @Override
    public List<Class<? > > loads( Name simpleName ) {
        List<Class<? > > classes = this.loadsByName( simpleName );
        this.loadsInClassTrait0( simpleName, false, classes );
        return classes;
    }

    @Override
    public Class<? > loadByName( Name simpleName ) throws ClassNotFoundException {
        return (Class<? >) this.loads0( simpleName, true );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<Class<? > > loadsByName( Name simpleName ) {
        try{
            return (List<Class<? > >) this.loads0( simpleName, false );
        }
        catch ( ClassNotFoundException e ) {
            return null; // This should never be happened.
        }
    }

    @Override
    public Class<? >  loadInClassTrait( Name simpleName ) throws ClassNotFoundException {
        return (Class<? >)this.loadsInClassTrait0( simpleName, true, null );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<Class<? > > loadsInClassTrait( Name simpleName ) {
        return (List<Class<? > >)this.loadsInClassTrait0( simpleName, false, new ArrayList<>() );
    }

    protected abstract boolean isAnnotationQualified( Annotation that, String szName );

    protected Object loadsInClassTrait0( Name simpleName, boolean bOnlyFirst, List<Class<? > > batch ) {
        this.updateScope();

        for( Map.Entry<String, CtClass > kv : this.mLoadedClassesPool.entrySet() ) {
            CtClass pc = kv.getValue();
            Annotation[] annotations = this.mClassInspector.queryVisibleAnnotations( pc );
            if( annotations != null ) {
                for( Annotation annotation : annotations ) {
                    if( this.isAnnotationQualified( annotation, simpleName.getName() ) ) {
                        try{
                            Class<?> c = this.mClassLoader.loadClass( kv.getKey() );
                            if( bOnlyFirst ) {
                                return c;
                            }
                            else {
                                batch.add( (Class<? >)c );
                            }
                        }
                        catch ( ClassNotFoundException e ) {
                            this.handleIgnoreException( e );
                        }
                    }
                }
            }
        }

        return batch;
    }

    @Override
    protected List<String > expandNamespace( Name name ) {
        if( name instanceof MultiScopeName) {
            return ((MultiScopeName) name).getFullNames();
        }

        return List.of( name.getFullName() ) ;
    }

    @Override
    protected void registerDefaultFilters() {

    }

    @Override
    protected abstract Class<? > loadSingleByFullClassName( String szFullClassName );

    @Override
    public MultiTraitClassLoader updateScope() {
        try{
            List<String > candidates = new ArrayList<>();
            for ( ScopedPackage scope : this.mClassScope.getAllScopes() ) {
                String szPkgName = scope.packageName();
                if( this.mVisitedClasses.contains( szPkgName ) ) {
                    continue;
                }
                else {
                    this.mVisitedClasses.add( szPkgName );
                }

                try {
                    this.mClassScanner.scan( szPkgName, true, candidates );
                }
                catch ( IOException e ) {
                    this.handleIgnoreException( e );
                }
            }

            for( String ns : candidates ) {
                this.mLoadedClassesPool.put( ns, this.mClassPool.get( ns ) );
            }
        }
        catch ( NotFoundException e ) {
            this.handleIgnoreException( e );
        }

        return this;
    }


    @Override
    public void clearCache() {
        this.mLoadedClassesPool.clear();
        this.mVisitedClasses.clear();
    }
}
