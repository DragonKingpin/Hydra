package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.hydra.storage.file.external.ExternalFile;

public interface ExternalUFileChannel extends UFileChannel {

    @Override
    ExternalFile getFile();
}
