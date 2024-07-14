package com.pinecone.framework.util.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ArchClassScopeSet implements ClassScope {
    protected Set<ScopedPackage > mScopes;
    protected ClassLoader         mClassLoader;

    protected ArchClassScopeSet( Set<ScopedPackage > scope, ClassLoader classLoader ) {
        this.mScopes        = scope;
        this.mClassLoader   = classLoader;
    }

    @Override
    public void addScope( String szPackageName ) {
        ScopedPackage pkg = ScopedPackage.defaultInstance( szPackageName, this.mClassLoader );
        this.mScopes.add( pkg );
    }

    @Override
    public void addScope( ScopedPackage scope ) {
        this.mScopes.add( scope );
    }

    @Override
    public void removeScope( String szPackageName ) {
        ScopedPackage that = this.getPackageByName( szPackageName );

        if( that != null ) {
            this.mScopes.remove( that );
        }
    }

    @Override
    public void removeScope( ScopedPackage scope ) {
        this.mScopes.remove( scope );
    }

    @Override
    public boolean containsScope( String szPackageName ) {
        return this.getPackageByName( szPackageName ) != null;
    }

    @Override
    public boolean containsScope( ScopedPackage scope ) {
        return this.mScopes.contains( scope );
    }

    @Override
    public ScopedPackage getPackageByName( String szPackageName ) {
        for( ScopedPackage pkg : this.mScopes ) {
            if( pkg.packageName().equals( szPackageName ) ){
                return pkg;
            }
        }
        return null;
    }

    @Override
    public List<ScopedPackage > getAllScopes() {
        return new ArrayList<>( this.mScopes );
    }

    @Override
    public List<String > getAllNameScopes() {
        List<String> list = new ArrayList<>();

        for( ScopedPackage pkg : this.mScopes ) {
            list.add( pkg.packageName() );
        }
        return list;
    }
}