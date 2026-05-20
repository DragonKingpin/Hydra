package com.walnut.odin.proc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcessStatus;

public interface ProcessRemoteEventHandler extends Pinenut {

    void fired( long pmClientId, UProcessStatus event, Object caused );

}