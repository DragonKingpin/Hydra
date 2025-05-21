package com.pinecone.hydra.task.kom.instance.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.dto.GenericInstance;
import com.pinecone.hydra.task.kom.instance.dto.Instance;

import java.util.List;

public interface InstanceMappingManipulator extends Pinenut {

    void insert( Instance instance );

    void update( Instance instance );

    Instance queryByGuid(GUID guid );

    Instance queryByName(String name );

    int countInstance();

    long countInstanceByName( String name );

    List<GenericInstance> fetchInstances(long offset, long pageSize );
    default List<GenericInstance> fetchInstances() {
        return this.fetchInstances( 0, this.countInstance() );
    }

    List<GenericInstance> queryByTaskGuid( GUID guid );

    long countInstanceByTaskGuid(GUID taskGuid);


}
