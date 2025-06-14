package com.walnut.odin.task.entity;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.ups.RavenInstance;

import java.util.Map;

public class GenericRavenInstance extends ArchInstance implements RavenInstance {
    public GenericRavenInstance(Identification instanceId, InstanceEntry instanceEntry, Map<String, Object> metaDataScope) {
        super(instanceId, instanceEntry, metaDataScope);
    }

    public GenericRavenInstance(Identification instanceId, InstanceEntry instanceEntry) {
        super(instanceId, instanceEntry);
    }

    @Override
    public void start() {
        // 首先要判断任务执行参数是否完全
    }
}
