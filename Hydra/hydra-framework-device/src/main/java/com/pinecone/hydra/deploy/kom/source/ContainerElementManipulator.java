package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ContainerElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface ContainerElementManipulator extends GUIDNameManipulator {

    void insert(  ContainerElement quickElement );

    ContainerElement getContainerElement( GUID guid, DeployInstrument deployInstrument );

    void update( ContainerElement serviceElement);


    void remove( GUID guid );


    List< ContainerElement> fetchContainerElementByName( String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );
}
