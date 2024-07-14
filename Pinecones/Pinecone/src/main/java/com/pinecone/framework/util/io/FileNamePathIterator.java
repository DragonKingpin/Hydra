package com.pinecone.framework.util.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

public class FileNamePathIterator extends PathItemIterator implements Iterator<Path > {
    public FileNamePathIterator( Path root, boolean recursive, boolean ignoreException ) throws IOException {
        super( root, recursive, ignoreException );
    }

    public FileNamePathIterator( Path root, boolean recursive ) throws IOException {
        this( root, recursive, false );
    }

    public FileNamePathIterator( Path root ) throws IOException {
        this( root, true );
    }

    @Override
    public Path next() {
        return super.next().getFileName();
    }
}
