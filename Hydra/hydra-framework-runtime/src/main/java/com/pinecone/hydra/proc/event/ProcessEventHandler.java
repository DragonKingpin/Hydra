package com.pinecone.hydra.proc.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.image.EntryPointRunnable;

public interface ProcessEventHandler extends Pinenut {

    void fired( EntryPointRunnable runnable, ProcessEvent event );

}
