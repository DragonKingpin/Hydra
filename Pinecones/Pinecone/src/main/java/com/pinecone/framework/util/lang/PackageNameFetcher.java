package com.pinecone.framework.util.lang;

import java.util.List;

public class PackageNameFetcher extends ArchMultiProtocolNamespaceFetcher implements NamespaceCollector {
    protected PathNamespaceCollectum   mFileAdapter;
    protected PathNamespaceCollectum   mJarAdapter;

    public PackageNameFetcher ( List<PathNamespaceCollectum > collectors, ClassLoader classLoader ) {
        super( collectors, classLoader );
    }

    public PackageNameFetcher ( List<PathNamespaceCollectum > collectors ) {
        super( collectors );
    }

    public PackageNameFetcher ( PathNamespaceCollectum fileAdapter, PathNamespaceCollectum jarAdapter, ClassLoader classLoader ) {
        super( fileAdapter, jarAdapter, classLoader );
    }

    public PackageNameFetcher ( ClassLoader classLoader ) {
        this( new FilePackageCollectorAdapter(), new JarPackageCollectorAdapter(), classLoader );
    }

    public PackageNameFetcher () {
        this( Thread.currentThread().getContextClassLoader() );
    }
}
