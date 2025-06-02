package com.walnut.odin.conduct;

import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.system.Hydrogen;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.ups.conduct.TaskRegimentDomain;

public class RavenCollectiveTaskRegiment implements CollectiveTaskRegiment {

    protected Hydrogen                  mSystem;

    protected CentralizedTaskInstrument mTaskInstrument;

    protected ProcessManager            mProcessManager;

    protected TaskRegimentDomain        mTaskRegimentDomain;



}
