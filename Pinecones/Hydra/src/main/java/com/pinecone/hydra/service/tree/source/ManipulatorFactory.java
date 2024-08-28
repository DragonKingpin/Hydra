package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ManipulatorFactory extends Pinenut {
    String DefaultServiceFamilyTreeManipulator  = "com.pinecone.hydra.service.tree.source.ServiceFamilyTreeManipulator";

    String DefaultServiceMetaManipulator        = "com.pinecone.hydra.service.tree.source.ServiceMetaManipulator";

    String DefaultServiceNodeManipulator        = "com.pinecone.hydra.service.tree.source.ServiceNodeManipulator";

    Class<? > getUniformObjectManipulator( String loaderName ) ;

    void register( String loaderName, Class<? > loader ) ;

    void deregister( String loaderName );

    int size();

    boolean isEmpty();
}
