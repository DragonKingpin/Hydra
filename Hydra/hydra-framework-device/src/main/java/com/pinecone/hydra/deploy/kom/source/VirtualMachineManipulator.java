package com.pinecone.hydra.deploy.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface VirtualMachineManipulator extends GUIDNameManipulator {

    void insert( VirtualMachineElement virtualMachineElement );

    VirtualMachineElement getDeployNode( GUID guid, DeployInstrument instrument );

    void update( VirtualMachineElement serviceElement );

    void remove( GUID guid );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );

}
