package com.pinecone.hydra.proc.image.path;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public final class ImagePosixPath {

    private ImagePosixPath() {
    }

    public static String normalize( URI uri ) {
        if ( uri == null ) {
            throw new IllegalArgumentException( "Image URI is null." );
        }
        return normalize( uri.getPath() );
    }

    public static String normalize( String path ) {
        if ( path == null ) {
            throw new IllegalArgumentException( "Image path is null." );
        }
        String szPath = path.trim().replace( '\\', '/' );
        if ( szPath.isEmpty() ) {
            throw new IllegalArgumentException( "Image path is empty." );
        }
        boolean bAbsolute = szPath.startsWith( "/" );
        String[] segments = szPath.split( "/+" );
        Deque<String> normalized = new ArrayDeque<>();
        for ( String segment : segments ) {
            if ( segment == null || segment.isEmpty() || ".".equals( segment ) ) {
                continue;
            }
            if ( "..".equals( segment ) ) {
                throw new IllegalArgumentException( "Image path must not contain `..`: " + path );
            }
            normalized.addLast( segment );
        }
        if ( normalized.isEmpty() ) {
            return bAbsolute ? "/" : "";
        }
        StringBuilder builder = new StringBuilder();
        if ( bAbsolute ) {
            builder.append( '/' );
        }
        Iterator<String> iterator = normalized.iterator();
        while ( iterator.hasNext() ) {
            builder.append( iterator.next() );
            if ( iterator.hasNext() ) {
                builder.append( '/' );
            }
        }
        return builder.toString();
    }

    public static String join( String parent, String child ) {
        String szParent = normalize( parent );
        String szChild = normalize( child );
        if ( szChild.startsWith( "/" ) ) {
            throw new IllegalArgumentException( "Child image path must be relative: " + child );
        }
        if ( "/".equals( szParent ) ) {
            return normalize( szParent + szChild );
        }
        return normalize( szParent + "/" + szChild );
    }
}
