package com.walnut.odin.task.obj;

import com.pinecone.hydra.task.ArchTaskInstance;
import com.pinecone.hydra.task.Task;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.ups.RavenTaskInstance;

public class GenericRavenTaskInstance extends ArchTaskInstance implements RavenTaskInstance {

    public GenericRavenTaskInstance( InstanceEntry instanceEntry ) {
        super( instanceEntry );
    }


    @Override
    public void start() {
        // 首先要判断任务执行参数是否完全
    }

    @Override
    public Object getProcessObject() {
        return null;
    }

    @Override
    public Task getAffiliatedTask() {
        return null;
    }

}
