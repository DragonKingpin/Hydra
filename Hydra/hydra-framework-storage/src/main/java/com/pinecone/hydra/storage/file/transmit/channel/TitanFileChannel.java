package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.io.FatFileStore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class TitanFileChannel implements UFileChannel {
    protected final KOMFileSystem  mFileSystem;
    protected final FileNode       mFileNode;
    protected final UFileOpenOption mOpenOption;
    protected final FatFileStore   mFatFileStore;
    protected boolean              mClosed;
    protected boolean              mWritten;
    protected boolean              mDirty;
    protected boolean              mCommitted;
    protected boolean              mAborted;
    protected long                 mPosition;

    public TitanFileChannel(
            KOMFileSystem fileSystem,
            FileNode fileNode,
            UFileOpenOption openOption,
            FatFileStore fatFileStore
    ) {
        this.mFileSystem   = fileSystem;
        this.mFileNode     = fileNode;
        this.mOpenOption   = openOption;
        this.mFatFileStore = fatFileStore;
        this.mPosition     = openOption == UFileOpenOption.APPEND ? fileNode.getDefinitionSize() : 0L;
    }

    @Override
    public FileNode getFileNode() {
        return this.mFileNode;
    }

    @Override
    public UFileStatus stat() {
        return new UFileStatus( this.mFileNode );
    }

    @Override
    public long size() {
        return this.mFileNode.getDefinitionSize();
    }

    @Override
    public long position() {
        this.assertOpen();
        return this.mPosition;
    }

    @Override
    public UFileChannel position( long newPosition ) {
        this.assertOpen();
        if ( newPosition < 0L ) {
            throw new IllegalArgumentException( "Negative UOFS channel position: " + newPosition );
        }
        this.mPosition = newPosition;
        return this;
    }

    @Override
    public int read( ByteBuffer dst ) throws IOException {
        this.assertReadable();
        int read = this.mFatFileStore.read( this.mFileNode, this.mPosition, dst );
        this.mPosition += read;
        return read;
    }

    @Override
    public int read( ByteBuffer dst, long position ) throws IOException {
        this.assertReadable();
        return this.mFatFileStore.read( this.mFileNode, position, dst );
    }

    @Override
    public long read( OutputStream outputStream ) throws IOException {
        this.assertReadable();
        return this.mFatFileStore.read( this.mFileNode, outputStream );
    }

    @Override
    public long read( long position, long length, OutputStream outputStream ) throws IOException {
        this.assertReadable();
        this.mPosition = position + length;
        return this.mFatFileStore.read( this.mFileNode, position, length, outputStream );
    }

    @Override
    public long read( UFileReadRequest request ) throws IOException {
        return this.read( request.getPosition(), request.getLength(), request.getOutputStream() );
    }

    @Override
    public long write( InputStream inputStream, long size ) throws IOException {
        this.assertWritable();
        this.assertSequentialWrite();
        long written;
        if ( this.mOpenOption == UFileOpenOption.APPEND || this.mWritten ) {
            written = this.mFatFileStore.append( this.mFileNode, inputStream, size );
        }
        else {
            written = this.mFatFileStore.write( this.mFileNode, inputStream, size );
        }
        this.mWritten = true;
        this.mDirty = true;
        this.mPosition += written;
        return written;
    }

    @Override
    public long write( UFileWriteRequest request ) throws IOException {
        return this.write( request.getInputStream(), request.getSize() );
    }

    @Override
    public int write( ByteBuffer src ) throws IOException {
        this.assertWritable();
        this.assertSequentialWrite();
        int written;
        if ( this.mOpenOption == UFileOpenOption.APPEND || this.mWritten ) {
            written = this.mFatFileStore.append( this.mFileNode, src );
        }
        else {
            written = this.mFatFileStore.write( this.mFileNode, src );
        }
        this.mWritten = true;
        this.mDirty = true;
        this.mPosition += written;
        return written;
    }

    @Override
    public long append( InputStream inputStream, long size ) throws IOException {
        this.assertAppendable();
        this.mPosition = this.mFileNode.getDefinitionSize();
        long written = this.mFatFileStore.append( this.mFileNode, inputStream, size );
        this.mDirty = true;
        this.mWritten = true;
        this.mPosition += written;
        return written;
    }

    @Override
    public long append( UFileAppendRequest request ) throws IOException {
        return this.append( request.getInputStream(), request.getSize() );
    }

    @Override
    public int append( ByteBuffer src ) throws IOException {
        this.assertAppendable();
        this.mPosition = this.mFileNode.getDefinitionSize();
        int written = this.mFatFileStore.append( this.mFileNode, src );
        this.mDirty = true;
        this.mWritten = true;
        this.mPosition += written;
        return written;
    }

    @Override
    public void flush() throws IOException {
        this.assertOpen();
        this.mFatFileStore.flush( this.mFileNode );
    }

    @Override
    public void commit() throws IOException {
        this.assertOpen();
        if ( this.mAborted ) {
            throw new IllegalStateException( "UFileChannel has been aborted" );
        }
        this.mFatFileStore.commit( this.mFileNode );
        this.mCommitted = true;
        this.mDirty = false;
    }

    @Override
    public void abort() throws IOException {
        this.assertOpen();
        if ( this.mCommitted ) {
            throw new IllegalStateException( "UFileChannel has been committed" );
        }
        this.mFatFileStore.abort( this.mFileNode );
        this.mAborted = true;
        this.mDirty = false;
    }

    @Override
    public void delete() throws IOException {
        this.assertOpen();
        this.mFatFileStore.delete( this.mFileNode );
        if ( this.mFileSystem != null ) {
            this.mFileSystem.remove( this.mFileNode.getGuid() );
        }
    }

    @Override
    public boolean isOpen() {
        return !this.mClosed;
    }

    @Override
    public void close() throws IOException {
        if ( !this.mClosed && this.mDirty && !this.mCommitted && !this.mAborted ) {
            this.commit();
        }
        this.mClosed = true;
    }

    protected void assertOpen() {
        if ( this.mClosed ) {
            throw new IllegalStateException( "UFileChannel is closed" );
        }
    }

    protected void assertReadable() {
        this.assertOpen();
        if ( this.mOpenOption == UFileOpenOption.CREATE || this.mOpenOption == UFileOpenOption.CREATE_OVERWRITE ) {
            throw new IllegalStateException( "Channel is not opened for read" );
        }
    }

    protected void assertWritable() {
        this.assertOpen();
        if ( this.mOpenOption != UFileOpenOption.CREATE && this.mOpenOption != UFileOpenOption.CREATE_OVERWRITE ) {
            throw new IllegalStateException( "Channel is not opened for write" );
        }
    }

    protected void assertSequentialWrite() {
        if ( this.mPosition != this.mFileNode.getDefinitionSize() ) {
            throw new UnsupportedOperationException( "Random write is not supported by UOFS channel" );
        }
    }

    protected void assertAppendable() {
        this.assertOpen();
        if ( this.mOpenOption != UFileOpenOption.APPEND ) {
            throw new IllegalStateException( "Channel is not opened for append" );
        }
    }
}
