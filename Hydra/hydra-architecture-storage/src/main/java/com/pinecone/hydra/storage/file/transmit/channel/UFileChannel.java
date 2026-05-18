package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface UFileChannel extends Pinenut, AutoCloseable {
    FileNode getFileNode();

    UFileStatus stat();

    long size() throws IOException;

    long position() throws IOException;

    UFileChannel position( long newPosition ) throws IOException;

    int read( ByteBuffer dst ) throws IOException;

    int read( ByteBuffer dst, long position ) throws IOException;

    long read( OutputStream outputStream ) throws IOException;

    long read( long position, long length, OutputStream outputStream ) throws IOException;

    long read( UFileReadRequest request ) throws IOException;

    long write( InputStream inputStream, long size ) throws IOException;

    long write( UFileWriteRequest request ) throws IOException;

    int write( ByteBuffer src ) throws IOException;

    long append( InputStream inputStream, long size ) throws IOException;

    long append( UFileAppendRequest request ) throws IOException;

    int append( ByteBuffer src ) throws IOException;

    void flush() throws IOException;

    void commit() throws IOException;

    void abort() throws IOException;

    void delete() throws IOException;

    boolean isOpen();

    @Override
    void close() throws IOException;
}
