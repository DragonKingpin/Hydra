package com.pinecone.hydra.storage.file.remove;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UofsRemoveProgressListener extends Pinenut {
    void onProgress( long totalCount, long doneCount, String currentPath, String message );
}
