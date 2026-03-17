package com.pinecone.framework.util.lang;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import com.pinecone.framework.util.lang.iterator.DirectoryFileIterator;
import com.pinecone.framework.util.lang.iterator.DirectoryPackageIterator;
import com.pinecone.framework.util.lang.iterator.JarFileIterator;
import com.pinecone.framework.util.lang.iterator.JarPackageIterator;

public class GenericScopeNSProtocolIteratorsFactory implements NSProtocolIteratorsFactoryAdapter {
    protected ClassLoader                 mClassLoader        ;
    protected ClassScope                  mSearchScope        ;
    protected String                      mszSuffix           ;

    public GenericScopeNSProtocolIteratorsFactory( ClassLoader classLoader, ClassScope searchScope, String szSuffix ) {
        this.mClassLoader = classLoader;
        this.mSearchScope = searchScope;
        this.mszSuffix    = szSuffix;
    }

    protected NamespaceIteratorPair newIteratorPair  ( URL url, String szNSName ) throws IOException {
        String protocol = url.getProtocol ();

        if ( protocol.equals ( NamespaceCollector.KEY_FILE_PROTOCOL ) ) {
            return new NamespaceIteratorPair(
                    new DirectoryFileIterator( url.getPath (), szNSName, this.mszSuffix ), new DirectoryPackageIterator( url.getPath (), szNSName, this.mszSuffix )
            );
        }
        else if ( protocol.equals ( NamespaceCollector.KEY_JAR_PROTOCOL ) ) {
            return new NamespaceIteratorPair(
                    new JarFileIterator( url.getPath (), this.mszSuffix ), new JarPackageIterator( url.getPath (), this.mszSuffix )
            );
        }

        return null;
    }

    @Override
    public void prepareScopeIterators ( String szNSName, List<NamespaceIteratorPair> pairs ) throws IOException {
        List<ScopedPackage > scope = null;
        if( this.mSearchScope != null ) {
            scope = this.mSearchScope.getAllScopes();

            for( ScopedPackage pkg : scope ) {
                this.prepareIterators( pkg.packageName() + NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR + szNSName, pairs );
            }
        }

        if( this.mSearchScope == null || scope.isEmpty() ) {
            this.prepareIterators( szNSName, pairs );
        }
    }

    @Override
    public void prepareIterators ( String szNSName, List<NamespaceIteratorPair> pairs ) throws IOException {
        String packagePath          = szNSName.replace ( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, NamespaceCollector.RESOURCE_NAME_SEPARATOR );
        Enumeration<URL > resources = this.mClassLoader.getResources( packagePath );
        if ( !resources.hasMoreElements() ) {
            return;
        }

        while ( resources.hasMoreElements() ) {
            URL url = resources.nextElement();
            NamespaceIteratorPair pair = this.newIteratorPair( url, szNSName );

            if( pair != null ) {
                pairs.add( pair );
            }
        }
    }
}