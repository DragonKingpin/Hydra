package com.pinecone.hydra.task;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.Namespace;

public interface TaskExtraMeta extends Pinenut {

    TaskFamilyMeta getKernelMeta();

    GUID getGuid() ;

    String getTaskName();

}
