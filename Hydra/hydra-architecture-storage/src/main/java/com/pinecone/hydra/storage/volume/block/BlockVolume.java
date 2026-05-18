package com.pinecone.hydra.storage.volume.block;

import com.pinecone.hydra.storage.volume.core.Volume;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface BlockVolume extends Volume {
    int read( long position, ByteBuffer dst ) throws IOException;

    int write( long position, ByteBuffer src ) throws IOException;
}
