package com.pinecone.hydra.storage.file.reparse;

public class ReparseDepthExceededException extends ReparseException {
    public ReparseDepthExceededException( int maxDepth, String path ) {
        super( "UOFS symbolic reparse depth exceeded, maxDepth=" + maxDepth + ", path=" + path );
    }
}
