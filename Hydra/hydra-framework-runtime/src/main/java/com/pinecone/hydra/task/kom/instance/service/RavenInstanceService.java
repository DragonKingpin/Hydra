package com.pinecone.hydra.task.kom.instance.service;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.dto.GenericInstance;
import com.pinecone.hydra.task.kom.instance.dto.Instance;
import com.pinecone.hydra.task.kom.instance.source.InstanceMappingManipulator;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;

import java.util.List;

public class RavenInstanceService implements InstanceService {

    protected InstanceMappingManipulator instanceManipulator;

    protected UniformTaskInstrument  uniformTaskInstrument;


    @Override
    public void addInstance(Instance instance) {
        this.instanceManipulator.insert(instance);
    }

    @Override
    public void updateInstance(Instance instance) {
        this.instanceManipulator.update(instance);
    }


    @Override
    public Instance queryInstance(String instanceName) {
        return this.instanceManipulator.queryByName(instanceName);
    }

    @Override
    public List<GenericInstance> queryInstances(String taskTreePath, long offset, long pageSize) {
        GUID guid = this.uniformTaskInstrument.queryGUIDByPath(taskTreePath);
        if ( guid == null ) {
            return null;
        }
        return this.instanceManipulator.queryByTaskGuid( guid );
    }


    @Override
    public long countInstanceByGuid(GUID taskGuid) {
        return this.instanceManipulator.countInstanceByTaskGuid(taskGuid);
    }
}
