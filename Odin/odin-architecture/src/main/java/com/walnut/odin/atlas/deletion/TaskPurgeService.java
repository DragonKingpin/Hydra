package com.walnut.odin.atlas.deletion;

import com.pinecone.framework.system.prototype.Pinenut;

public interface TaskPurgeService extends Pinenut {

    TaskPurgeSafetyReport check( TaskPurgeRequest request );

    TaskPurgeResult purge( TaskPurgeRequest request );
}
