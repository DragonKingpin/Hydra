package com.walnut.odin.formation.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.dto.FormationRunSubmitRequest;
import com.walnut.odin.formation.entity.RunEntry;

public interface RunService extends Pinenut {

    RunEntry createRun( GUID formationGuid );

    RunEntry createRun( FormationRunSubmitRequest request );
}
