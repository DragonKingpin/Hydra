package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.ServiceInstrument;
import com.pinecone.hydra.task.kom.entity.JobElement;

public interface ApplicationMetaManipulator extends Pinenut {
    void insert(JobElement jobElement);

    void remove(GUID guid);

    JobElement getApplicationElement(GUID guid, ServiceInstrument serviceInstrument);

    void update(JobElement jobElement);
}
