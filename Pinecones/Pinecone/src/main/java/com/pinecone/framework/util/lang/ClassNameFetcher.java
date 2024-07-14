package com.pinecone.framework.util.lang;

import java.util.List;

public class ClassNameFetcher extends ArchMultiProtocolNamespaceFetcher implements NamespaceCollector {
    protected PathNamespaceCollectum   mFileAdapter;
    protected PathNamespaceCollectum   mJarAdapter;

    public ClassNameFetcher ( List<PathNamespaceCollectum > collectors, ClassLoader classLoader ) {
        super( collectors, classLoader );
    }

    public ClassNameFetcher ( List<PathNamespaceCollectum > collectors ) {
        super( collectors );
    }

    public ClassNameFetcher ( PathNamespaceCollectum fileAdapter, PathNamespaceCollectum jarAdapter, ClassLoader classLoader ) {
        super( fileAdapter, jarAdapter, classLoader );
    }

    public ClassNameFetcher ( ClassLoader classLoader ) {
        this( new FileClassCollectorAdapter(), new JarClassCollectorAdapter(), classLoader );
    }

    public ClassNameFetcher () {
        this( Thread.currentThread().getContextClassLoader() );
    }
}
