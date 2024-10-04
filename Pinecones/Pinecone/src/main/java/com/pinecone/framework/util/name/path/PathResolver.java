package com.pinecone.framework.util.name.path;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public interface PathResolver extends Pinenut {
    List<String > resolvePath      ( String[] parts ) ;

    String        resolvePath      ( String path );

    List<String > resolvePathParts ( String path ) ;

    String[] segmentPathParts      ( String path ) ;

    String assemblePath            ( List<String > parts ) ;
}
