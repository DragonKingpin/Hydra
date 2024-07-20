package com.pinecone.framework.util.lang.iterator;
import com.pinecone.framework.util.lang.JarUtils;
import com.pinecone.framework.util.lang.NamespaceCollector;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarEntry;


public class JarClassIterator extends ArchJarEntryIterator implements NamespaceIterator {
    public JarClassIterator( String szResourcePath ) throws IOException {
        super( szResourcePath );
    }

    @Override
    public boolean hasNext() {
        return this.mCurrentEntry != null;
    }

    @Override
    public String next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }

        String entryName = this.mCurrentEntry.getName();
        String className = JarUtils.normalizeJarClassName( entryName, this.mClassesScopePath );

        this.skipEntries();

        return className;
    }

    @Override
    public void forEachRemaining( Consumer<? super String> action ) {
        Objects.requireNonNull( action );
        while ( this.hasNext() ) {
            action.accept( this.next() );
        }
    }

    @Override
    protected void skipEntries() {
        while ( this.mEntries.hasMoreElements() ) {
            JarEntry entry   = this.mEntries.nextElement();
            String entryName = entry.getName();
            //Debug.trace( entryName );
            if ( entryName.endsWith( ".class" ) ) {
                int index = entryName.lastIndexOf( NamespaceCollector.RESOURCE_NAME_SEPARATOR );
                String myPackagePath;
                if ( index == -1 ) {
                    myPackagePath = entryName;
                }
                else {
                    myPackagePath = entryName.substring( 0, index );
                }

                if( this.mClassesScopePath == null ) {
                    if ( myPackagePath.equals( this.mPackagePath ) ) {
                        this.mCurrentEntry = entry;
                        return;
                    }
                }
                else {
                    if ( myPackagePath.startsWith( this.mClassesScopePath ) && myPackagePath.endsWith( this.mPackagePath ) ) {
                        this.mCurrentEntry = entry;
                        return;
                    }
                }
            }
        }
        this.mCurrentEntry = null; // No more valid entries
    }
}