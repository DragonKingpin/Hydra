package com.pinecone.framework.util.lang.iterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarEntry;

public class JarEntryIterator extends ArchJarEntryIterator implements Iterator<JarEntry > {
    public JarEntryIterator( String szResourcePath ) throws IOException {
        super( szResourcePath );
    }

    @Override
    public boolean hasNext() {
        return this.mEntries.hasMoreElements();
    }

    @Override
    public JarEntry next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }

        this.mCurrentEntry = this.mEntries.nextElement();

        return this.mCurrentEntry;
    }

    @Override
    public void forEachRemaining( Consumer<? super JarEntry> action ) {
        Objects.requireNonNull( action );
        while ( this.hasNext() ) {
            action.accept( this.next() );
        }
    }

    @Override
    protected void skipEntries() {

    }
}
