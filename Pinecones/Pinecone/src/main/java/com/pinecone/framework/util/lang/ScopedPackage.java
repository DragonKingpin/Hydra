package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;

public interface ScopedPackage extends Pinenut, Comparable<ScopedPackage > {
    String parentName() ;

    ScopedPackage parent() ;

    List<ScopedPackage > children() ;

    List<String > fetchChildrenNames() ;

    String packageName() ;

    ClassLoader getClassLoader() ;

    NamespaceCollector getPackageCollector() ;

    List<String > fetchChildrenClassNames();

    String fetchFirstClassName();

    Package getPackage();

    boolean hasLoaded();

    Package tryLoad();

    static ScopedPackage defaultInstance( String packageName, ClassLoader classLoader ) {
        return new LazyScopedPackage( packageName, classLoader );
    }

    static ScopedPackage defaultInstance( String packageName ) {
        return new LazyScopedPackage( packageName );
    }

    @Override
    default int compareTo( ScopedPackage o ){
        return this.packageName().compareTo( o.packageName() );
    }
}
