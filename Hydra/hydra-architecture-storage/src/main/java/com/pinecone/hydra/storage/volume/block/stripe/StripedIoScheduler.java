package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.volume.block.StripedVolume;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface StripedIoScheduler extends Pinenut {
    int read( StripedVolume volume, long position, ByteBuffer dst ) throws IOException;

    int write( StripedVolume volume, long position, ByteBuffer src ) throws IOException;
}
