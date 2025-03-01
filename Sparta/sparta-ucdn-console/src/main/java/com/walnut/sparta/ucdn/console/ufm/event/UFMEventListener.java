package com.walnut.sparta.ucdn.console.ufm.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;

public interface UFMEventListener extends Pinenut {
    void afterEventTriggered( String path, String serviceId, FileNode fileNode );
}
