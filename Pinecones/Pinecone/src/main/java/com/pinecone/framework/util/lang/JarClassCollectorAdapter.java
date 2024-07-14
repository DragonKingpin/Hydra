package com.pinecone.framework.util.lang;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        String[] jarInfo   = szResourcePath.split ( "!" );
        String jarFilePath = jarInfo[0].substring ( jarInfo[0].indexOf ( NamespaceCollector.RESOURCE_NAME_SEPARATOR ) );
        String packagePath = szResourcePath;
        if( jarInfo.length > 1 ) {
            packagePath = jarInfo[1].substring ( 1 );
        }

        try {
            JarFile jarFile = new JarFile( jarFilePath );
            Enumeration<JarEntry> entries = jarFile.entries ();
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
                        if ( myPackagePath.equals ( packagePath ) ) {
                            entryName = entryName.replace ( NamespaceCollector.RESOURCE_NAME_SEPARATOR, "." ).substring ( 0, entryName.lastIndexOf ( "." ) );

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
            e.printStackTrace ();
        }

        return null;
    }

}
