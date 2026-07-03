package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.digest.TaskTreeElementDigest;

import java.util.List;

public interface TaskTreeDigestManipulator extends Pinenut {

    TaskTreeElementDigest queryDigestByPath( String szPath );

    TaskTreeElementDigest queryDigestByGuid( GUID guid );

    List<TaskTreeElementDigest> fetchChildDigests( GUID parentGuid );
}
