package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.deploy.PhysicalHost;

public interface PhysicalHostManipulator extends Pinenut {

    void insert(PhysicalHost physicalHost);


}
