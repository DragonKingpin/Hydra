package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;

public interface QuickElementManipulator extends Pinenut {

    void insert( QuickElement quickElement );

    QuickElement getDeployNode(GUID guid, DeployInstrument deployInstrument);

    void update(QuickElement serviceElement);

    void remove(GUID guid);
}
