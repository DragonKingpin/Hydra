package com.pinecone.framework.util.lang;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public abstract class ArchMultiProtocolNamespaceFetcher implements NamespaceCollector {
    protected List<PathNamespaceCollectum > mCollectors;
    protected ClassLoader                   mClassLoader;
    protected PathNamespaceCollectum        mFileAdapter;
    protected PathNamespaceCollectum        mJarAdapter;


    public ArchMultiProtocolNamespaceFetcher ( List<PathNamespaceCollectum > collectors, ClassLoader classLoader ) {
        this.mCollectors  = collectors;
        this.mClassLoader = classLoader;

        for( PathNamespaceCollectum collectum : collectors ) {
            if( collectum.matched( NamespaceCollector.KEY_FILE_PROTOCOL ) ) {
                this.mFileAdapter = collectum;
            }
            else if( collectum.matched( NamespaceCollector.KEY_JAR_PROTOCOL ) ) {
                this.mJarAdapter = collectum;
            }
        }
    }

    public ArchMultiProtocolNamespaceFetcher ( List<PathNamespaceCollectum > collectors ) {
        this( collectors, Thread.currentThread().getContextClassLoader() );
    }

    public ArchMultiProtocolNamespaceFetcher ( PathNamespaceCollectum fileAdapter, PathNamespaceCollectum jarAdapter, ClassLoader classLoader ) {
        this( new ArrayList<>(), classLoader );

        this.mFileAdapter = fileAdapter;
        this.mJarAdapter  = jarAdapter;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @Override
    public void fetch ( String szNSName, List<String > collections, boolean bCollectChildPackage ) {
        this.fetch0( szNSName, collections, bCollectChildPackage );
    }

    @Override
    public String fetchFirst( String szNSName ) {
        return this.fetch0( szNSName, null, false );
    }

    public String fetch0 ( String szNSName, List<String > collections, boolean bCollectChildPackage ) {
        String packagePath = szNSName.replace ( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, NamespaceCollector.RESOURCE_NAME_SEPARATOR );
        URL url = this.mClassLoader.getResource ( packagePath );
        if ( url == null ) {
            if( this.mClassLoader instanceof URLClassLoader ) {
                String first = this.fetchByURLs( ((URLClassLoader) this.mClassLoader).getURLs(), szNSName, packagePath, collections, bCollectChildPackage );
                if( collections == null ) {
                    return first;
                }
            }
            else {
                return null;
            }
        }

        if( collections != null ) {
            this.fetch( url, szNSName, collections, bCollectChildPackage );
        }
        else {
            return this.fetchFirst( url, szNSName );
        }

        return null;
    }

    public String fetchByURLs( URL[] urls, String szNSName, String szPackagePath, List<String> collections, boolean bCollectChildPackage ) {
        if ( urls != null ) {
            for ( int i = 0; i < urls.length; i++ ) {
                URL url = urls[i];
                String urlPath = url.getPath();
                if ( urlPath.endsWith( "classes/" ) ) {
                    continue;
                }

                String jarPath = urlPath + "!/" + szPackagePath;
                //List<String > subList = UnitUtils.spawnExtendParent( collections );
                if( collections != null ) {
                    this.mJarAdapter.collect( jarPath, szNSName, collections, bCollectChildPackage );
                }
                else {
                    return this.mJarAdapter.collectFirst( jarPath, szNSName );
                }
                //classNames.addAll( subList );
            }
        }

        return null;
    }

    @Override
    public void fetch( URL url, String szNSName, List<String> collections, boolean bCollectChildPackage ) {
        String protocol = url.getProtocol ();
        if ( protocol.equals ( NamespaceCollector.KEY_FILE_PROTOCOL ) ) {
            this.mFileAdapter.collect ( url.getPath (), szNSName, collections, bCollectChildPackage );
        }
        else if ( protocol.equals ( NamespaceCollector.KEY_JAR_PROTOCOL ) ) {
            this.mJarAdapter.collect ( url.getPath (), szNSName, collections, bCollectChildPackage );
        }
        else {
            for( PathNamespaceCollectum collectum : this.mCollectors ) {
                if( collectum.matched( protocol ) ) {
                    collectum.collect( url.getPath (), szNSName, collections, bCollectChildPackage );
                }
            }
        }
    }

    @Override
    public String fetchFirst( URL url, String szNSName ) {
        String protocol = url.getProtocol ();
        if ( protocol.equals ( NamespaceCollector.KEY_FILE_PROTOCOL ) ) {
            return this.mFileAdapter.collectFirst ( url.getPath (), szNSName );
        }
        else if ( protocol.equals ( NamespaceCollector.KEY_JAR_PROTOCOL ) ) {
            return this.mJarAdapter.collectFirst ( url.getPath (), szNSName );
        }
        else {
            for( PathNamespaceCollectum collectum : this.mCollectors ) {
                if( collectum.matched( protocol ) ) {
                    return collectum.collectFirst( url.getPath (), szNSName );
                }
            }
        }

        return null;
    }
}
