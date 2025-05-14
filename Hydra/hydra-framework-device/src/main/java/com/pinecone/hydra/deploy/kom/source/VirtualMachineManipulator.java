package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.VirtualMachine;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface VirtualMachineManipulator extends GUIDNameManipulator {

    /*void insert( VirtualMachine virtualMachine );*/

    void insert( VirtualMachineElement virtualMachineElement );

    VirtualMachineElement getDeployNode(GUID guid, DeployInstrument instrument);

    void update(VirtualMachineElement serviceElement);

    void remove(GUID guid);
}
