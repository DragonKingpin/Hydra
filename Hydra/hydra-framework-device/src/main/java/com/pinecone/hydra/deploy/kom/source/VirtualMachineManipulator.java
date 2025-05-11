package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.deploy.VirtualMachine;

public interface VirtualMachineManipulator extends Pinenut {

    void insert( VirtualMachine virtualMachine );
}
