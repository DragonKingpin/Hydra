package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;
import com.walnut.odin.task.RavenTaskInstance;

public class LaunchedContext implements Pinenut {

    protected UProcess process;

    protected RavenTaskInstance taskInstance;

    public LaunchedContext( UProcess process, RavenTaskInstance taskInstance ) {
        this.process = process;
        this.taskInstance = taskInstance;
    }

    public RavenTaskInstance getTaskInstance() {
        return this.taskInstance;
    }
    public void setTaskInstance( RavenTaskInstance taskInstance ) {
        this.taskInstance = taskInstance;
    }

    public UProcess getProcess() {
        return this.process;
    }

    public void setProcess( UProcess process ) {
        this.process = process;
    }

}
