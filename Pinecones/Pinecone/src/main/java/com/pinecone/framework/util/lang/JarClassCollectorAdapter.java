package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.util.lang.iterator.JarEntryIterator;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

public class JarClassCollectorAdapter implements PathNamespaceCollectum {
    @Override
    public boolean matched( String szProtocol ) {
        return szProtocol.toLowerCase().equals( NamespaceCollector.KEY_JAR_PROTOCOL );
    }

    @Override
    public void collect         ( String szResourcePath, String szPackageName, List<String > classNames, boolean bCollectChildren ) {
        this.collect0( szResourcePath, szPackageName, classNames, bCollectChildren );
    }

    @Override
    public String collectFirst  ( String szResourcePath, String szPackageName ) {
        return this.collect0( szResourcePath, szPackageName, null, false );
    }

    public String collect0 ( String szResourcePath, String szPackageName, List<String > classNames, boolean bCollectChildren ) {
        try {
            JarEntryIterator iterator        = new JarEntryIterator( szResourcePath );
            Enumeration<JarEntry> entries    = iterator.entries ();
            String packagePath               = iterator.getPackagePath();
            String classesScopePath          = iterator.getClassesScopePath();

            while ( entries.hasMoreElements () ) {
                JarEntry jarEntry = entries.nextElement ();
                String entryName = jarEntry.getName ();
                if ( entryName.endsWith ( ".class" ) ) {
                    if ( bCollectChildren && classNames != null ) { // [@Harold Notice] No need for recursion, for JAR files, this flag is usually processed in a tiled manner
                        if ( entryName.startsWith ( packagePath ) ) {
                            entryName = entryName.replace ( NamespaceCollector.RESOURCE_NAME_SEPARATOR, "." ).substring ( 0, entryName.lastIndexOf ( "." ) );
                            classNames.add ( entryName );
                        }
                    }
                    else {
                        int index = entryName.lastIndexOf ( NamespaceCollector.RESOURCE_NAME_SEPARATOR );
                        String myPackagePath;
                        if ( index != -1 ) {
                            myPackagePath = entryName.substring ( 0, index );
                        }
                        else {
                            myPackagePath = entryName;
                        }

                        boolean bQualified = false;
                        if( classesScopePath == null ) {
                            if( myPackagePath.equals( packagePath ) ) {
                                bQualified = true;
                            }
                        }
                        else {
                            if ( myPackagePath.startsWith( classesScopePath ) && myPackagePath.endsWith( packagePath ) ) {
                                bQualified = true;
                            }
                        }

                        if ( bQualified ) {
                            entryName = JarUtils.normalizeJarClassName( entryName, classesScopePath );

                            if( classNames == null ) {
                                return entryName;
                            }
                            else {
                                classNames.add ( entryName );
                            }
                        }
                    }
                }
            }
        }
        catch ( IOException e ) {
            throw new ProxyProvokeHandleException( e );
        }

        return null;
    }

}
