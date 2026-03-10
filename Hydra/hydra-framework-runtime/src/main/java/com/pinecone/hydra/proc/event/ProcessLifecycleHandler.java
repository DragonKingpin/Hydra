package com.pinecone.hydra.proc.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.image.EntryPointRunnable;

public interface ProcessLifecycleHandler extends Pinenut {

    void fired( String imageAddress, EntryPointRunnable runnable, ProcessEvent event );

}
