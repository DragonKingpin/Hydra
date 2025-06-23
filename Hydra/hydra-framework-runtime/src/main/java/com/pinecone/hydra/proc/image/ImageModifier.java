package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.Unsafe;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.event.ProcessEventHandler;

public interface ImageModifier extends Pinenut {

    @Unsafe
    void addSystemProcessEventHandler( EntryPointRunnable runnable, ProcessEventHandler handler );

    @Unsafe
    void removeSystemProcessEventHandler( EntryPointRunnable runnable, ProcessEventHandler handler );

    @Unsafe
    int querySystemProcessEventHandlersSize( EntryPointRunnable runnable );

}
