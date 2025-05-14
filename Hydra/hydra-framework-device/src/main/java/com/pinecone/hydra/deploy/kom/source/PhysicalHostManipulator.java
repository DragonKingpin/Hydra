package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.PhysicalHost;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.PhysicalHostElement;

public interface PhysicalHostManipulator extends Pinenut {

   /* void insert(PhysicalHost physicalHost);*/

    void insert(PhysicalHostElement physicalHostElement);

    PhysicalHostElement getDeployNode(GUID guid, DeployInstrument deployInstrument);

    void update(PhysicalHostElement serviceElement);

    void remove(GUID guid);
}
