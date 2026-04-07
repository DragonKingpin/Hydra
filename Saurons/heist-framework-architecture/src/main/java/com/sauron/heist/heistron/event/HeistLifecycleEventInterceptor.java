package com.sauron.heist.heistron.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.heistron.orchestration.Hierarchy;

public interface HeistLifecycleEventInterceptor extends Pinenut {

    void afterLifecycleEventTriggered( String name, Heistum heist, TaskInstanceStatus instanceStatus, Hierarchy hierarchy );

}
