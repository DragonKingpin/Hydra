package com.pinecone.hydra.device.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.VirtualMachineElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface VirtualMachineManipulator extends GUIDNameManipulator {

    void insert( VirtualMachineElement virtualMachineElement );

    VirtualMachineElement getDeviceNode( GUID guid, DeviceInstrument instrument );

    void update( VirtualMachineElement serviceElement );

    void remove( GUID guid );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );

}
