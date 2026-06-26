package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.entity.RunEntry;

public interface RunManipulator extends Pinenut {
    int insert( RunEntry run );

    RunEntry selectByGuid( GUID guid );

    List<RunEntry> fetchRunnableRuns( int limit );

    long countRuns( GUID formationGuid, String strategyType, String runStatus );

    List<RunEntry> pageRuns( GUID formationGuid, String strategyType, String runStatus, long offset, long limit );

    int updateStatus( GUID guid, String status );

    int updateRunningTerminalStatus( GUID guid, String status );

    int markRunning( GUID guid );

    int increaseSubmitted( GUID guid );

    int increaseCompleted( GUID guid );

    int increaseFailed( GUID guid );

    int removeByFormationGuids( List<GUID> formationGuids );
}
