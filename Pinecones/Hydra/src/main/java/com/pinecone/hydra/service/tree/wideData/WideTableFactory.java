package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.system.prototype.Pinenut;

public interface WideTableFactory extends Pinenut {
    String DefaultApplicationNode   =  "com.pinecone.hydra.service.tree.nodes.GenericApplicationNode";
    String DefaultServiceNode       =  "com.pinecone.hydra.service.tree.nodes.GenericServiceNode";


    NodeWideTable getUniformObjectWideTable( String loaderName ) ;

    void register( String loaderName, NodeWideTable loader ) ;

    void deregister( String loaderName );

    int size();

    boolean isEmpty();
}
