package com.pinecone.hydra.device.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.PhysicalHostElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface PhysicalHostManipulator extends GUIDNameManipulator {

   /* void insert(PhysicalHost physicalHost);*/

    void insert( PhysicalHostElement physicalHostElement );

    PhysicalHostElement getPhysicalHostElement( GUID guid, DeviceInstrument deviceInstrument );

    void update( PhysicalHostElement serviceElement );

    void remove( GUID guid );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );

}
