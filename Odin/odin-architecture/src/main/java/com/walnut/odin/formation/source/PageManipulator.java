package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.dto.FormationPageQuery;
import com.walnut.odin.formation.plan.FormationPage;

public interface PageManipulator extends Pinenut {
    int insert( FormationPage page );

    List<FormationPage> fetchPendingPages( GUID runGuid, long limit );

    List<FormationPage> listByRunGuid( GUID runGuid );

    long countPages( FormationPageQuery query );

    List<FormationPage> pagePages( FormationPageQuery query, long offset, long limit );

    int claimPage( long id, String claimOwner, GUID claimToken, long leaseSeconds );

    int markRunning( long id );

    int markCompleted( long id );

    int removeByFormationGuids( List<GUID> formationGuids );
}
