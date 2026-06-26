package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.dto.FormationFrameQuery;
import com.walnut.odin.formation.plan.FormationFrame;

public interface FrameManipulator extends Pinenut {
    int insert( FormationFrame frame );

    List<FormationFrame> fetchPendingFramesByPage( GUID runGuid, long pageNo );

    List<FormationFrame> fetchPendingFrames( GUID runGuid, long limit );

    int claimFrame( GUID guid, String claimOwner, GUID claimToken, long leaseSeconds );

    default long countFrames( GUID runGuid, String frameStatus ) {
        FormationFrameQuery query = new FormationFrameQuery();
        query.setRunGuid( runGuid );
        query.setFrameStatus( frameStatus );
        return this.countFrames( query );
    }

    long countFrames( FormationFrameQuery query );

    default List<FormationFrame> pageFrames( GUID runGuid, String frameStatus, long offset, long limit ) {
        FormationFrameQuery query = new FormationFrameQuery();
        query.setRunGuid( runGuid );
        query.setFrameStatus( frameStatus );
        return this.pageFrames( query, offset, limit );
    }

    List<FormationFrame> pageFrames( FormationFrameQuery query, long offset, long limit );

    int markSubmitted( GUID guid, GUID instanceGuid );

    int markCompleted( GUID guid );

    int markFailed( GUID guid, String errorCause );

    int markCompletedByInstanceGuid( GUID instanceGuid );

    int markFailedByInstanceGuid( GUID instanceGuid, String errorCause );

    int removeByFormationGuids( List<GUID> formationGuids );
}
