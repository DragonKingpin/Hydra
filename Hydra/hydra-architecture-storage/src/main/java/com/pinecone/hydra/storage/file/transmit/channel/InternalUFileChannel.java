package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.hydra.storage.file.entity.FileNode;

public interface InternalUFileChannel extends UFileChannel {

    @Override
    FileNode getFile();
}
