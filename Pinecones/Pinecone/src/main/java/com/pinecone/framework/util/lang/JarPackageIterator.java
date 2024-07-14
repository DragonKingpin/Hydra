package com.pinecone.framework.util.lang;

import com.pinecone.framework.util.StringUtils;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;

public class JarPackageIterator extends JarClassIterator {
    public JarPackageIterator( String szResourcePath ) throws IOException {
        super( szResourcePath );
    }

    @Override
    protected void skipEntries() {
        while ( this.mEntries.hasMoreElements() ) {
            JarEntry entry = this.mEntries.nextElement();
            String entryName = entry.getName();
            if ( entry.isDirectory() ) {
                if ( entryName.startsWith( this.mPackagePath ) ) {
                    String childSegment = entryName.substring( this.mPackagePath.length() );
                    if( StringUtils.countOccurrencesOf( childSegment, NamespaceCollector.RESOURCE_NAME_SEPARATOR, 3 ) > 2 ) {
                        continue;
                    }
                    if( entryName.equals( this.mPackagePath + NamespaceCollector.RESOURCE_NAME_SEPARATOR ) ) { // Self path
                        continue;
                    }

                    this.mCurrentEntry = entry;
                    return;
                }
            }
        }
        this.mCurrentEntry = null; // No more valid entries
    }

    @Override
    public String next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }

        String entryName   = this.mCurrentEntry.getName();
        String packageName = entryName.replace( NamespaceCollector.RESOURCE_NAME_SEPARATOR, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR ).substring( 0, entryName.length() - 1 );

        this.skipEntries();

        return packageName;
    }
}
