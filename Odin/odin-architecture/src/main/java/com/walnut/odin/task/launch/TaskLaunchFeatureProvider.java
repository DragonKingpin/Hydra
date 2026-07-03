package com.walnut.odin.task.launch;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.troll.LaunchFeature;

public interface TaskLaunchFeatureProvider extends Pinenut {

    boolean supports( LaunchProvideTaskParam param );

    LaunchFeature apply( LaunchProvideTaskParam param, LaunchFeature feature );

}
