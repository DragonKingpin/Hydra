package com.pinecone.hydra.device.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.GenericDeviceElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

public interface GenericDeviceManipulator extends GUIDNameManipulator {

    void insert( GenericDeviceElement genericDeviceElement );

    GenericDeviceElement getGenericDeviceElement( GUID guid, DeviceInstrument deviceInstrument );

    void update( GenericDeviceElement genericDeviceElement );

    void remove( GUID guid );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );
}
