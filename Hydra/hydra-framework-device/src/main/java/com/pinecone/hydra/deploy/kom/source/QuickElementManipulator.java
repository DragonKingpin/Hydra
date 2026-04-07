package com.pinecone.hydra.deploy.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.DeployElement;
import com.pinecone.hydra.deploy.kom.entity.QuickElement;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface QuickElementManipulator extends GUIDNameManipulator {

    void insert( QuickElement quickElement );

    QuickElement getQuickElement( GUID guid, DeployInstrument deployInstrument );

    void update( QuickElement serviceElement );

    void remove( GUID guid );


    List<QuickElement> fetchQuickElementByName( String name );

    @Override
    List<GUID> getGuidsByName( String name );

    @Override
    List<GUID> getGuidsByNameID( String name, GUID guid );
}
