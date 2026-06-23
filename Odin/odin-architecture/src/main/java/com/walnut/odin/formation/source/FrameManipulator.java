package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.plan.FormationFrame;

public interface FrameManipulator extends Pinenut {
    int insert( FormationFrame frame );

    List<FormationFrame> fetchPendingFramesByPage( GUID runGuid, long pageNo );

    int markSubmitted( GUID guid, GUID instanceGuid );

    int markFailed( GUID guid, String errorCause );
}
