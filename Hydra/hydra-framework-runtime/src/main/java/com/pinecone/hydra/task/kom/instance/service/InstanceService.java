package com.pinecone.hydra.task.kom.instance.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.dto.GenericInstance;
import com.pinecone.hydra.task.kom.instance.dto.Instance;

import java.util.List;

public interface InstanceService extends Pinenut {


    void addInstance( Instance instance );

    void updateInstance( Instance instance );


    Instance queryInstance(String instanceName );


    List<GenericInstance> queryInstances(String taskTreePath, long offset, long pageSize);

    long countInstanceByGuid(GUID taskGuid );
}
