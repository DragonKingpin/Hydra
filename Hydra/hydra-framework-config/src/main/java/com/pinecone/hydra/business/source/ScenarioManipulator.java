package com.pinecone.hydra.business.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericScenarioElement;
import com.pinecone.hydra.business.entity.ScenarioElement;

public interface ScenarioManipulator extends Pinenut {

    void insert( ScenarioElement scenarioElement );

    void remove( GUID guid );

    GenericScenarioElement get( GUID guid );

    void update( ScenarioElement scenarioElement );

    List<GenericScenarioElement > queryByScenarioCode( String szScenarioCode );
}
