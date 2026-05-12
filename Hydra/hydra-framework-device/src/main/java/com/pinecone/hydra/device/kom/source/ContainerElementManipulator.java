package com.pinecone.hydra.device.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ContainerElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface ContainerElementManipulator extends GUIDNameManipulator {

    void insert(  ContainerElement quickElement );

    ContainerElement getContainerElement( GUID guid, DeviceInstrument deviceInstrument );

    void update( ContainerElement serviceElement);


    void remove( GUID guid );


    List< ContainerElement> fetchContainerElementByName( String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );
}
