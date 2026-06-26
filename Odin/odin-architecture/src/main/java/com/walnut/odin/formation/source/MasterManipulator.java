package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.deletion.FormationGroupPurgeResult;

public interface MasterManipulator extends Pinenut {
    GroupManipulator groupManipulator();

    GroupTaskManipulator groupTaskManipulator();

    RunManipulator runManipulator();

    PageManipulator pageManipulator();

    FrameManipulator frameManipulator();

    FormationGroupPurgeResult purgeGroups( List<GUID> formationGuids );
}
