package com.walnut.odin.formation.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.dto.FormationRunSubmitRequest;
import com.walnut.odin.formation.dto.FormationRunSubmitResult;
import com.walnut.odin.formation.dto.FormationRuntimeSnapshot;

public interface FormationService extends Pinenut {

    FormationRunSubmitResult submitRun( FormationRunSubmitRequest request );

    boolean cancelRun( GUID runGuid );

    Object retrieveRun( GUID runGuid );

    FormationRuntimeSnapshot retrieveRuntimeSnapshot();
}
