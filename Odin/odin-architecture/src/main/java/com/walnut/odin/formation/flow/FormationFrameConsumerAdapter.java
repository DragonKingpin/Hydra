package com.walnut.odin.formation.flow;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationFrameFeedback;

public interface FormationFrameConsumerAdapter extends Pinenut {
    FormationFrameFeedback consumeFrame( FormationFrame frame ) throws Exception;
}
