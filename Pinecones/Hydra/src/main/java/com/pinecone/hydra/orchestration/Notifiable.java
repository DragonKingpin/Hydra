package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Notifiable extends Pinenut {
    void notice( BranchNoticeException e );
}
