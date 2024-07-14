package com.pinecone.framework.util.io;

import com.pinecone.framework.system.ProxyProvokeHandleException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class PathItemIterator implements PathIterator {
    private Stack<DirectoryStream<Path > > mDirStack  = new Stack<>() ;
    private Stack<Iterator<Path > >        mIterStack = new Stack<>() ;
    private Path                           mNextPath  = null          ;
    private boolean                        mbRecursive                ;
    private boolean                        mbIgnoreException          ;

    public PathItemIterator(Path root, boolean recursive, boolean ignoreException ) throws IOException {
        this.mbRecursive       = recursive;
        this.mbIgnoreException = ignoreException;

        if ( root != null && Files.exists(root) ) {
            if ( Files.isDirectory( root ) ) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(root);
                this.mDirStack.push(stream);
                this.mIterStack.push(stream.iterator());
            }
            else {
                this.mNextPath = root;
            }
        }
    }

    public PathItemIterator(Path root, boolean recursive ) throws IOException {
        this( root, recursive, false );
    }

    public PathItemIterator(Path root ) throws IOException {
        this( root, true );
    }



    @Override
    public boolean hasNext() {
        if ( this.mNextPath != null ) {
            return true;
        }

        while ( !this.mIterStack.isEmpty() ) {
            Iterator<Path > iter = this.mIterStack.peek();

            if ( iter.hasNext() ) {
                Path file = iter.next();
                if ( Files.isDirectory(file) ) {
                    this.mNextPath = file;
                    if ( this.mbRecursive ) {
                        try {
                            DirectoryStream<Path > stream = Files.newDirectoryStream(file);
                            this.mDirStack.push( stream );
                            this.mIterStack.push( stream.iterator() );
                        }
                        catch ( IOException e ) {
                            if( !this.mbIgnoreException ) {
                                throw new ProxyProvokeHandleException( e );
                            }
                        }
                    }
                    return true;
                }
                else {
                    this.mNextPath = file;
                    return true;
                }
            }
            else {
                this.mIterStack.pop();
                try {
                    this.mDirStack.pop().close();
                }
                catch ( IOException e ) {
                    if( !this.mbIgnoreException ) {
                        throw new ProxyProvokeHandleException( e );
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Path next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException( "No more files" );
        }

        Path result = this.mNextPath;
        this.mNextPath = null;
        return result;
    }
}
