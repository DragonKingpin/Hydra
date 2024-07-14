package com.pinecone.framework.util.lang;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

public class GenericScopeNSProtocolIteratorsFactory implements NSProtocolIteratorsFactoryAdapter {
    protected ClassLoader                 mClassLoader        ;
    protected ClassScope                  mSearchScope        ;

    public GenericScopeNSProtocolIteratorsFactory( ClassLoader classLoader, ClassScope searchScope ) {
        this.mClassLoader = classLoader;
        this.mSearchScope = searchScope;
    }

    protected ClassIteratorPair  newIteratorPair  ( URL url, String szNSName ) throws IOException {
        String protocol = url.getProtocol ();

        if ( protocol.equals ( NamespaceCollector.KEY_FILE_PROTOCOL ) ) {
            return new ClassIteratorPair(
                    new DirectoryClassIterator  ( url.getPath (), szNSName ), new DirectoryPackageIterator( url.getPath (), szNSName )
            );
        }
        else if ( protocol.equals ( NamespaceCollector.KEY_JAR_PROTOCOL ) ) {
            return new ClassIteratorPair(
                    new JarClassIterator  ( url.getPath () ), new JarPackageIterator( url.getPath () )
            );
        }

        return null;
    }

    @Override
    public void prepareScopeIterators ( String szNSName, List<ClassIteratorPair > pairs ) throws IOException {
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
    public void prepareIterators ( String szNSName, List<ClassIteratorPair > pairs ) throws IOException {
        String packagePath          = szNSName.replace ( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, NamespaceCollector.RESOURCE_NAME_SEPARATOR );
        Enumeration<URL > resources = getClass().getClassLoader().getResources(packagePath);
        if ( !resources.hasMoreElements() ) {
            return;
        }

        while ( resources.hasMoreElements() ) {
            URL url = resources.nextElement();
            ClassIteratorPair pair = this.newIteratorPair( url, szNSName );

            if( pair != null ) {
                pairs.add( pair );
            }
        }
    }
}
