package com.pinecone.hydra.unit.vgraph.algo;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;

public interface DAGPathResolver extends Pinenut {
    List<String > resolvePath      (String[] parts ) ;

    String        resolvePath      ( String path );

    List<String > resolvePathParts ( String path ) ;

    String[] segmentPathParts      ( String path ) ;

    String assemblePath            ( List<String > parts ) ;
}
