package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.util.name.MultiNamespace;
import com.pinecone.framework.util.name.Name;

import java.util.ArrayList;
import java.util.List;

public abstract class ArchClassScopeLoader implements MultiClassScopeLoader {
    protected ClassScope           mClassScope;
    protected ClassLoader          mClassLoader;
    protected List<ClassFilter >   mIncludeFilters;
    protected List<ClassFilter >   mExcludeFilters;

    protected ArchClassScopeLoader( ClassScope classScope, ClassLoader classLoader ) {
        this.mClassScope      = classScope;
        this.mClassLoader     = classLoader;
        this.mIncludeFilters  = new ArrayList<>();
        this.mExcludeFilters  = new ArrayList<>();
    }

    @Override
    public void addIncludeFilter( ClassFilter includeFilter ) {
        this.mIncludeFilters.add(includeFilter);
    }

    @Override
    public void addExcludeFilter( ClassFilter excludeFilter ) {
        this.mExcludeFilters.add(0, excludeFilter);
    }

    @Override
    public void resetFilters( boolean useDefaultFilters ) {
        this.mIncludeFilters.clear();
        this.mExcludeFilters.clear();
        if ( useDefaultFilters ) {
            this.registerDefaultFilters();
        }
    }

    protected void registerDefaultFilters() {

    }

    protected boolean filter( Class<?> clazz ) {
        for( ClassFilter filter : this.mIncludeFilters ) {
            if( !filter.match( clazz, this ) ){
                return true;
            }
        }

        for( ClassFilter filter : this.mExcludeFilters ) {
            if( filter.match( clazz, this ) ){
                return true;
            }
        }

        return false;
    }

    @Override
    public Class<? > load( Name name ) throws ClassNotFoundException {
        return (Class<?>) this.loads0( name, true );
    }

    @Override
    public List loads( Name name ) {
        try{
            return (List) this.loads0( name, false );
        }
        catch ( ClassNotFoundException e ) {
            return null; // This should never be happened.
        }
    }

    protected List<String > expandNamespace( Name name ) {
        if( name instanceof MultiNamespace) {
            return ((MultiNamespace) name).getFullNames();
        }

        return List.of( name.getFullName() ) ;
    }

    protected abstract Class<? > loadSingleByFullClassName( String szFullClassName );

    protected Object loads0( Name name, boolean bOnlyFirst ) throws ClassNotFoundException {
        List<Class<? > > batch = null;
        if( !bOnlyFirst ) {
            batch = new ArrayList<>();
        }

        List<String > ns = this.expandNamespace( name );
        for ( ScopedPackage scope : this.mClassScope.getAllScopes() ) {
            String className = scope.packageName() + NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR;

            for( String szNS : ns ) {
                String szCN = className + szNS;
                Class<? > ste = this.loadSingleByFullClassName( szCN );
                if( bOnlyFirst ) {
                    if( ste == null ) {
                        throw new ClassNotFoundException( "Servgram class not found: " + szCN );
                    }
                    return ste;
                }
                else {
                    if( ste != null ) {
                        batch.add( ste );
                    }
                }
            }
        }

        return batch;
    }

    protected void handleIgnoreException( Exception e ) throws ProvokeHandleException {
        // Just ignore them.
    }
}
