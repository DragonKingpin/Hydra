package com.pinecone.hydra.proc.image;

import com.pinecone.framework.system.prototype.Pinenut;

public interface EntryPointRunnableFactory extends Pinenut {

    EntryPointRunnable create();

}
