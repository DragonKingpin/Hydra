package com.walnut.odin.formation.source;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.formation.entity.GenericRun;
import com.walnut.odin.formation.entity.RunEntry;

@Mapper
@IbatisDataAccessObject
public interface FormationRunMapper extends RunManipulator {
    int insert( @Param( "run" ) RunEntry run );

    GenericRun selectByGuid( @Param( "guid" ) GUID guid );

    List<GenericRun> fetchRunnableRuns0( @Param( "limit" ) int limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<RunEntry> fetchRunnableRuns( int limit ) {
        return (List) this.fetchRunnableRuns0( limit );
    }

    long countRuns(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "strategyType" ) String strategyType,
            @Param( "runStatus" ) String runStatus );

    List<GenericRun> pageRuns0(
            @Param( "formationGuid" ) GUID formationGuid,
            @Param( "strategyType" ) String strategyType,
            @Param( "runStatus" ) String runStatus,
            @Param( "offset" ) long offset,
            @Param( "limit" ) long limit );

    @Override
    @SuppressWarnings( "unchecked" )
    default List<RunEntry> pageRuns( GUID formationGuid, String strategyType, String runStatus, long offset, long limit ) {
        return (List)this.pageRuns0( formationGuid, strategyType, runStatus, offset, limit );
    }

    int updateStatus( @Param( "guid" ) GUID guid, @Param( "status" ) String status );

    int updateRunningTerminalStatus( @Param( "guid" ) GUID guid, @Param( "status" ) String status );

    int markRunning( @Param( "guid" ) GUID guid );

    int increaseSubmitted( @Param( "guid" ) GUID guid );

    int increaseCompleted( @Param( "guid" ) GUID guid );

    int increaseFailed( @Param( "guid" ) GUID guid );

    int removeByFormationGuids( @Param( "formationGuids" ) List<GUID> formationGuids );
}
