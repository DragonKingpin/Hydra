package com.walnut.odin.proc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.event.ProcessEvent;

public interface ProcessRemoteEventHandler extends Pinenut {

    void fired( long pmClientId, ProcessEvent event, Object caused );

}