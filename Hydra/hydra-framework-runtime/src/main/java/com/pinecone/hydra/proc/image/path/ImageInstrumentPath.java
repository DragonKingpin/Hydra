package com.pinecone.hydra.proc.image.path;

public final class ImageInstrumentPath {

    private ImageInstrumentPath() {
    }

    public static String normalize( String path ) {
        String szPath = ImagePosixPath.normalize( path );
        while ( szPath.startsWith( "/" ) ) {
            szPath = szPath.substring( 1 );
        }
        if ( szPath.isEmpty() ) {
            throw new IllegalArgumentException( "Image instrument path is empty." );
        }
        return szPath;
    }

    public static String join( String parent, String child ) {
        String szParent = normalize( parent );
        String szChild = normalize( child );
        if ( szChild.contains( "/" ) ) {
            throw new IllegalArgumentException( "Image name must be a single path segment: " + child );
        }
        return szParent + "/" + szChild;
    }
}
