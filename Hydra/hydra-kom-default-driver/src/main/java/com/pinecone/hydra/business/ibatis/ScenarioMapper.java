package com.pinecone.hydra.business.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericScenarioElement;
import com.pinecone.hydra.business.entity.ScenarioElement;
import com.pinecone.hydra.business.source.ScenarioManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface ScenarioMapper extends ScenarioManipulator {

    @Override
    void insert( ScenarioElement scenarioElement );

    @Override
    void remove( @Param( "guid" ) GUID guid );

    @Override
    GenericScenarioElement get( @Param( "guid" ) GUID guid );

    @Override
    void update( ScenarioElement scenarioElement );

    @Override
    List<GenericScenarioElement > queryByScenarioCode( @Param( "scenarioCode" ) String szScenarioCode );
}
