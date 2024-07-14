package com.pinecone.framework.util.lang;

import java.util.ArrayList;
import java.util.List;

public class LazyScopedPackage implements ScopedPackage {
    protected String                      mszPackageName;
    protected ClassLoader                 mClassLoader;
    protected NamespaceCollector          mPackageCollector;
    protected NamespaceCollector          mClassCollector;
    protected ScopedPackage               mParent;
    protected List<ScopedPackage >        mChildren;
    protected Package                     mPackage;

    public LazyScopedPackage( String packageName, ScopedPackage parent, ClassLoader classLoader, NamespaceCollector packageCollector, NamespaceCollector classCollector ) {
        this.mParent           = parent;
        this.mszPackageName    = packageName;
        this.mClassLoader      = classLoader;
        this.mPackageCollector = packageCollector;
        this.mClassCollector   = classCollector;
    }

    public LazyScopedPackage( String packageName, ScopedPackage parent, ClassLoader classLoader ) {
        this( packageName, parent, classLoader, null, null );

        this.mPackageCollector = new PackageNameFetcher( this.mClassLoader );
    }

    public LazyScopedPackage( String packageName, ScopedPackage parent ) {
        this( packageName, parent, Thread.currentThread().getContextClassLoader() );
    }

    public LazyScopedPackage( String packageName, ClassLoader classLoader ) {
        this( packageName, null, classLoader );
    }

    public LazyScopedPackage( String packageName ) {
        this( packageName, (ScopedPackage) null );
    }

    @Override
    public String parentName() {
        int lastDotIndex = mszPackageName.lastIndexOf('.');
        if ( lastDotIndex == -1 ) {
            return null;
        }
        return mszPackageName.substring( 0, lastDotIndex );
    }

    @Override
    public ScopedPackage parent() {
        if( this.mParent == null ) {
            String parentName = this.parentName();
            if ( parentName == null ) {
                return null;
            }

            this.mParent = new LazyScopedPackage( parentName, null, this.mClassLoader, this.mPackageCollector, this.mClassCollector );
        }

        return this.mParent;
    }

    @Override
    public List<ScopedPackage > children() {
        if( this.mChildren == null ) {
            this.mChildren = new ArrayList<>();

            List<String > namesList = this.getPackageCollector().fetch( this.packageName(), false );
            for( String name : namesList ) {
                this.mChildren.add( new LazyScopedPackage( name, this, this.mClassLoader, this.mPackageCollector, this.mClassCollector ) );
            }
        }

        return this.mChildren;
    }

    @Override
    public List<String > fetchChildrenNames() {
        List<ScopedPackage > children = this.mChildren;
        if( children == null ) {
            children = this.children();
        }

        List<String > namesList = new ArrayList<>();
        for( ScopedPackage scopedPackage : children ) {
            namesList.add( scopedPackage.packageName() );
        }
        return namesList;
    }

    @Override
    public List<String > fetchChildrenClassNames() {
        if( this.mClassCollector == null ) {
            this.mClassCollector = new ClassNameFetcher( this.getClassLoader() );
        }

        return this.mClassCollector.fetch( this.packageName(), false );
    }

    @Override
    public String fetchFirstClassName() {
        if( this.mClassCollector == null ) {
            this.mClassCollector = new ClassNameFetcher( this.getClassLoader() );
        }

        return this.mClassCollector.fetchFirst( this.packageName() );
    }

    @Override
    public String packageName() {
        return mszPackageName;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @Override
    public NamespaceCollector getPackageCollector() {
        return this.mPackageCollector;
    }

    @Override
    public boolean hasLoaded() {
        if( this.mPackage != null ) {
            return true;
        }

        Package pkg = this.getClassLoader().getDefinedPackage( this.packageName() );
        if ( pkg != null ) {
            this.mPackage = pkg;
            return true;
        }

        return false;
    }

    @Override
    public Package tryLoad() {
        if( this.mPackage != null ) {
            return this.mPackage;
        }

        String szFirstClass = this.fetchFirstClassName();
        if( szFirstClass != null ) {
            try{
                Class<?> cls = this.mClassLoader.loadClass( szFirstClass );
                if( cls == null ) {
                    return null;
                }
                this.mPackage = this.getPackage();
            }
            catch ( Exception e ) {
                this.mPackage = null;
            }
        }

        return this.mPackage;
    }

    @Override
    public Package getPackage() {
        if( this.mPackage == null ) {
            this.mPackage = this.getClassLoader().getDefinedPackage( this.packageName() );
        }
        return this.mPackage;
    }

    @Override
    public String toString() {
        return this.mszPackageName;
    }

    @Override
    public String toJSONString() {
        return "\"" + this.toString() + "\"";
    }
}
