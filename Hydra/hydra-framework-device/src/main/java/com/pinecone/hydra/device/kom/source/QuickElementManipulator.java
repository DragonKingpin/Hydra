package com.pinecone.hydra.device.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.DeviceElement;
import com.pinecone.hydra.device.kom.entity.QuickElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface QuickElementManipulator extends GUIDNameManipulator {

    void insert( QuickElement quickElement );

    QuickElement getQuickElement( GUID guid, DeviceInstrument deviceInstrument );

    void update( QuickElement serviceElement );

    void remove( GUID guid );


    List<QuickElement> fetchQuickElementByName( String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );
}
