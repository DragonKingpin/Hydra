package com.pinecone.framework.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FileIterator implements Iterator<File > {
    private PathIterator mPathIterator;

    public FileIterator( PathIterator iterator ) {
        this.mPathIterator = iterator;
    }

    public FileIterator( File root, boolean recursive, boolean ignoreException ) throws IOException {
        this.mPathIterator = new PathItemIterator( root.toPath(), recursive, ignoreException );
    }

    public FileIterator( File root, boolean recursive ) throws IOException {
        this( root, recursive, false );
    }

    public FileIterator( File root ) throws IOException {
        this( root, true );
    }

    @Override
    public boolean hasNext() {
        return this.mPathIterator.hasNext();
    }

    @Override
    public File next() {
        Path path = this.mPathIterator.next();
        if ( path == null ) {
            throw new NoSuchElementException( "No more files" );
        }
        return path.toFile();
    }
}
