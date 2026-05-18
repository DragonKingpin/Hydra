package com.pinecone.hydra.storage.volume.io;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface PhysicalAccessor extends Pinenut {
    GUID getGuid();

    String getName();

    VolumePhysicalType getPhysicalType();

    long getCapacity();

    long getCommittedBytes() throws IOException;

    int read( long position, ByteBuffer dst ) throws IOException;

    int write( long position, ByteBuffer src ) throws IOException;

    void flush() throws IOException;

    void close() throws IOException;
}

