package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.entity.RunEntry;

public interface RunManipulator extends Pinenut {
    int insert( RunEntry run );

    RunEntry selectByGuid( GUID guid );

    List<RunEntry> fetchRunnableRuns( int limit );

    int updateStatus( GUID guid, String status );

    int markRunning( GUID guid );

    int increaseSubmitted( GUID guid );

    int increaseCompleted( GUID guid );

    int increaseFailed( GUID guid );
}
