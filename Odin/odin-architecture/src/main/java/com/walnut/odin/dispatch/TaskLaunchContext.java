package com.walnut.odin.dispatch;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.troll.LaunchFeature;

public interface TaskLaunchContext extends Pinenut {

    LaunchFeature getLaunchFeature();

    RavenTaskInstance getTaskInstance();


}
