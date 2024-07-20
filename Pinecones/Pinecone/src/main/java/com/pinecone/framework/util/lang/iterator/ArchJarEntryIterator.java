package com.pinecone.framework.util.lang.iterator;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.lang.JarUtils;
import com.pinecone.framework.util.lang.NamespaceCollector;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public abstract class ArchJarEntryIterator implements Pinenut {
    protected JarFile                  mJarFile;
    protected Enumeration<JarEntry >   mEntries;
    protected String                   mPackagePath;
    protected String                   mClassesScopePath = null;  // e.g. BOOT-INF/lib/
    protected JarEntry                 mCurrentEntry;

    public ArchJarEntryIterator( String szResourcePath ) throws IOException {
        String[] jarInfo   = szResourcePath.split ( "!" );
        String jarFilePath = jarInfo[0].substring ( jarInfo[0].indexOf ( NamespaceCollector.RESOURCE_NAME_SEPARATOR ) );

        this.mJarFile      = new JarFile( jarFilePath );

        boolean bUsingFile = true;
        String packagePath = szResourcePath;
        if( jarInfo.length > 1 ) {
            String szCurrentFragName = jarInfo[1].substring(1);
            if( jarInfo.length > 2 && szCurrentFragName.endsWith(".jar") ) {
                ZipEntry zipEntry = this.mJarFile.getEntry( szCurrentFragName );
                if( zipEntry == null ) {
                    throw new IOException( "Illegal resource path: " + szResourcePath );
                }

                Deque<JarInputStream> streamStack = new LinkedList<>();
                JarInputStream jarInputStream = new JarInputStream(
                        this.mJarFile.getInputStream( zipEntry )
                );
                streamStack.addFirst( jarInputStream );

                try{
                    if( jarInfo.length > 3 ) {
                        for ( int i = 2; i < jarInfo.length - 1; ++i ) {
                            if ( !jarInfo[i].toLowerCase().endsWith(".jar") ) {
                                break;
                            }

                            szCurrentFragName = jarInfo[i].substring(1);
                            JarEntry jarEntry;
                            while ( ( jarEntry = jarInputStream.getNextJarEntry() ) != null ) {
                                String szJarEntryName = jarEntry.getName();
                                if ( !jarEntry.isDirectory() && szJarEntryName.equals( szCurrentFragName ) ) {
                                    jarInputStream = new JarInputStream( this.mJarFile.getInputStream( jarEntry ) );
                                    streamStack.addFirst( jarInputStream );
                                    break;
                                }
                                jarInputStream.closeEntry();
                            }
                        }
                    }

                    bUsingFile = false;
                    // [@Harold Notice] Using a temporary enumeration to prevent unexpected resource leaks.
                    this.mEntries = JarUtils.fetchEnumeration( jarInputStream );
                }
                finally {
                    JarInputStream t;
                    // [@Harold Notice] All `JarInputStream` should be close and release in the nested scenario.
                    while ( ( t = streamStack.peek() ) != null ) {
                        t.close();
                        streamStack.pop();
                    }
                }

            }
            else{
                if( jarInfo.length != 2 ) {
                    this.mClassesScopePath = szCurrentFragName + "/";
                }
            }
            packagePath = jarInfo[ jarInfo.length - 1 ].substring ( 1 );
        }
        this.mPackagePath  = packagePath;

        if( bUsingFile ) {
            this.mEntries      = this.mJarFile.entries();
        }

        this.skipEntries();
    }

    public boolean hasNext() {
        return this.mCurrentEntry != null;
    }

    public Object next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }

        return this.mCurrentEntry;
    }

    public String getClassesScopePath() {
        return this.mClassesScopePath;
    }

    public String getPackagePath() {
        return this.mPackagePath;
    }

    public Enumeration<JarEntry > entries() {
        return this.mEntries;
    }

    protected abstract void skipEntries() ;
}
