package com.pinecone.hydra.storage.file.remove;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;

public interface UofsRemoveOperator extends Pinenut {
    UofsRemovePlan plan( UofsRemoveRequest request );

    UofsRemoveReport remove( UofsRemoveRequest request, @Nullable UofsRemoveProgressListener listener );

    default UofsRemoveReport remove( UofsRemoveRequest request ) {
        return this.remove( request, null );
    }
}
