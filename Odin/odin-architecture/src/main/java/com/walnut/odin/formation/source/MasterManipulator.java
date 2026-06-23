package com.walnut.odin.formation.source;

import com.pinecone.framework.system.prototype.Pinenut;

public interface MasterManipulator extends Pinenut {
    GroupManipulator groupManipulator();

    GroupTaskManipulator groupTaskManipulator();

    RunManipulator runManipulator();

    PageManipulator pageManipulator();

    FrameManipulator frameManipulator();
}
