package com.walnut.odin.conduct.schedule;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.dispatch.TaskDispatcher;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.troll.TaskExecutionLauncher;

public interface UniformTaskScheduler extends Pinenut {

    RavenTaskConfig ravenTaskConfig();

    CentralizedTaskInstrument taskInstrument();

    InstanceInstrument instanceInstrument();

    RuntimeAtlasInstrument atlasInstrument();

    TaskExecutionLauncher taskExecutionLauncher();

    TaskDispatcher taskDispatcher();

    String getPartitionName();


    TaskSchedulePreparator taskSchedulePreparator();

    InstanceScheduleImpetus instanceScheduleImpetus();

    InstanceScheduleAllocator instanceScheduleAllocator();

}
