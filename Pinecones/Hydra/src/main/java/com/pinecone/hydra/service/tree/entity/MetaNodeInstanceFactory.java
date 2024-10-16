package com.pinecone.hydra.service.tree.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;

public interface MetaNodeInstanceFactory extends Pinenut {
    String DefaultApplicationNode   =  GenericApplicationNode.class.getName();
    String DefaultServiceNode       =  GenericServiceNode.class.getName();


    MetaNodeInstance getUniformObjectWideTable(String loaderName ) ;

    void register( String loaderName, MetaNodeInstance loader ) ;

    void deregister( String loaderName );

    int size();

    boolean isEmpty();
}
