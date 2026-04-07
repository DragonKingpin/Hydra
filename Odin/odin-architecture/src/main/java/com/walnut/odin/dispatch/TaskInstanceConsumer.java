package com.walnut.odin.dispatch;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskInstanceConsumer extends Pinenut {

    void tryConsume( TaskLaunchContext context ) throws TaskConsumeException;

    ConsumeCompromisedPolice compromisedPolice();

}
