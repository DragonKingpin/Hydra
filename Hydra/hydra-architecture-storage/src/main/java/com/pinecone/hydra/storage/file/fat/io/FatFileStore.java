package com.pinecone.hydra.storage.file.fat.io;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface FatFileStore extends Pinenut {
    long write( FileNode fileNode, InputStream inputStream, long size ) throws IOException;

    int write( FileNode fileNode, ByteBuffer src ) throws IOException;

    long append( FileNode fileNode, InputStream inputStream, long size ) throws IOException;

    int append( FileNode fileNode, ByteBuffer src ) throws IOException;

    long read( FileNode fileNode, OutputStream outputStream ) throws IOException;

    long read( FileNode fileNode, long position, long length, OutputStream outputStream ) throws IOException;

    int read( FileNode fileNode, long position, ByteBuffer dst ) throws IOException;

    void flush( FileNode fileNode ) throws IOException;

    void commit( FileNode fileNode ) throws IOException;

    void abort( FileNode fileNode ) throws IOException;

    void delete( FileNode fileNode ) throws IOException;
}
