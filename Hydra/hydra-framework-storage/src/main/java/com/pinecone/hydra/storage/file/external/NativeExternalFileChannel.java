package com.pinecone.hydra.storage.file.external;

import com.pinecone.hydra.storage.file.transmit.channel.ExternalUFileChannel;
import com.pinecone.hydra.storage.file.transmit.channel.UFileAppendRequest;
import com.pinecone.hydra.storage.file.transmit.channel.UFileOpenOption;
import com.pinecone.hydra.storage.file.transmit.channel.UFileReadRequest;
import com.pinecone.hydra.storage.file.transmit.channel.UFileStatus;
import com.pinecone.hydra.storage.file.transmit.channel.UFileWriteRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class NativeExternalFileChannel implements ExternalUFileChannel {
    protected final ExternalFile    mExternalFile;
    protected final UFileOpenOption mOpenOption;
    protected final FileChannel     mChannel;
    protected boolean               mClosed;
    protected boolean               mDirty;
    protected long                  mPosition;

    public NativeExternalFileChannel( ExternalFile externalFile, UFileOpenOption openOption ) throws IOException {
        this.mExternalFile = externalFile;
        this.mOpenOption   = openOption;
        this.mChannel      = this.openChannel( externalFile, openOption );
        this.mPosition     = openOption == UFileOpenOption.APPEND ? this.mChannel.size() : 0L;
    }

    @Override
    public ExternalFile getFile() {
        return this.mExternalFile;
    }

    @Override
    public UFileStatus stat() {
        return new UFileStatus( this.mExternalFile );
    }

    @Override
    public long size() throws IOException {
        this.assertOpen();
        return this.mChannel.size();
    }

    @Override
    public long position() {
        this.assertOpen();
        return this.mPosition;
    }

    @Override
    public NativeExternalFileChannel position( long newPosition ) {
        this.assertOpen();
        if ( newPosition < 0L ) {
            throw new IllegalArgumentException( "Negative native file channel position: " + newPosition );
        }
        this.mPosition = newPosition;
        return this;
    }

    @Override
    public int read( ByteBuffer dst ) throws IOException {
        this.assertReadable();
        int read = this.mChannel.read( dst, this.mPosition );
        if ( read > 0 ) {
            this.mPosition += read;
        }
        return read;
    }

    @Override
    public int read( ByteBuffer dst, long position ) throws IOException {
        this.assertReadable();
        return this.mChannel.read( dst, position );
    }

    @Override
    public long read( OutputStream outputStream ) throws IOException {
        this.assertReadable();
        return this.read( 0L, this.mChannel.size(), outputStream );
    }

    @Override
    public long read( long position, long length, OutputStream outputStream ) throws IOException {
        this.assertReadable();
        if ( position < 0L || length < 0L ) {
            throw new IllegalArgumentException( "Negative native file read range: " + position + ", " + length );
        }
        long transferred = 0L;
        WritableByteChannel target = Channels.newChannel( outputStream );
        while ( transferred < length ) {
            long count = this.mChannel.transferTo( position + transferred, length - transferred, target );
            if ( count <= 0L ) {
                break;
            }
            transferred += count;
        }
        this.mPosition = position + transferred;
        return transferred;
    }

    @Override
    public long read( UFileReadRequest request ) throws IOException {
        return this.read( request.getPosition(), request.getLength(), request.getOutputStream() );
    }

    @Override
    public long write( InputStream inputStream, long size ) throws IOException {
        this.assertWritable();
        long written = this.transferFromInput( inputStream, size, this.mPosition );
        this.mPosition += written;
        this.mDirty = true;
        return written;
    }

    @Override
    public long write( UFileWriteRequest request ) throws IOException {
        return this.write( request.getInputStream(), request.getSize() );
    }

    @Override
    public int write( ByteBuffer src ) throws IOException {
        this.assertWritable();
        int written = this.mChannel.write( src, this.mPosition );
        if ( written > 0 ) {
            this.mPosition += written;
            this.mDirty = true;
        }
        return written;
    }

    @Override
    public long append( InputStream inputStream, long size ) throws IOException {
        this.assertAppendable();
        this.mPosition = this.mChannel.size();
        long written = this.transferFromInput( inputStream, size, this.mPosition );
        this.mPosition += written;
        this.mDirty = true;
        return written;
    }

    @Override
    public long append( UFileAppendRequest request ) throws IOException {
        return this.append( request.getInputStream(), request.getSize() );
    }

    @Override
    public int append( ByteBuffer src ) throws IOException {
        this.assertAppendable();
        this.mPosition = this.mChannel.size();
        int written = this.mChannel.write( src, this.mPosition );
        if ( written > 0 ) {
            this.mPosition += written;
            this.mDirty = true;
        }
        return written;
    }

    @Override
    public void flush() throws IOException {
        this.assertOpen();
        this.mChannel.force( true );
    }

    @Override
    public void commit() throws IOException {
        this.flush();
        this.mDirty = false;
    }

    @Override
    public void abort() {
        throw new UnsupportedOperationException( "Native external file channel does not support abort" );
    }

    @Override
    public void delete() throws IOException {
        this.assertOpen();
        this.mChannel.close();
        this.mClosed = true;
        Files.deleteIfExists( this.mExternalFile.getNativeFile().toPath() );
    }

    @Override
    public boolean isOpen() {
        return !this.mClosed;
    }

    @Override
    public void close() throws IOException {
        if ( !this.mClosed ) {
            if ( this.mDirty ) {
                this.commit();
            }
            this.mChannel.close();
            this.mClosed = true;
        }
    }

    protected FileChannel openChannel( ExternalFile externalFile, UFileOpenOption openOption ) throws IOException {
        Path path = externalFile.getNativeFile().toPath();
        Path parent = path.getParent();
        if ( parent != null && openOption != UFileOpenOption.READ ) {
            Files.createDirectories( parent );
        }
        if ( openOption == UFileOpenOption.READ ) {
            return FileChannel.open( path, StandardOpenOption.READ );
        }
        if ( openOption == UFileOpenOption.CREATE ) {
            return FileChannel.open( path, StandardOpenOption.CREATE_NEW, StandardOpenOption.READ, StandardOpenOption.WRITE );
        }
        if ( openOption == UFileOpenOption.CREATE_OVERWRITE ) {
            return FileChannel.open( path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.READ, StandardOpenOption.WRITE );
        }
        if ( openOption == UFileOpenOption.APPEND ) {
            return FileChannel.open( path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE );
        }
        throw new IllegalArgumentException( "Unsupported native external file open option: " + openOption );
    }

    protected long transferFromInput( InputStream inputStream, long size, long position ) throws IOException {
        if ( size < 0L ) {
            throw new IllegalArgumentException( "Negative native file write size: " + size );
        }
        long written = 0L;
        ByteBuffer buffer = ByteBuffer.allocate( 1024 * 1024 );
        ReadableByteChannel source = Channels.newChannel( inputStream );
        while ( written < size ) {
            buffer.clear();
            int limit = (int)Math.min( buffer.capacity(), size - written );
            buffer.limit( limit );
            int read = source.read( buffer );
            if ( read < 0 ) {
                break;
            }
            if ( read == 0 ) {
                break;
            }
            buffer.flip();
            while ( buffer.hasRemaining() ) {
                int count = this.mChannel.write( buffer, position + written );
                if ( count <= 0 ) {
                    break;
                }
                written += count;
            }
        }
        return written;
    }

    protected void assertOpen() {
        if ( this.mClosed ) {
            throw new IllegalStateException( "Native external file channel is closed" );
        }
    }

    protected void assertReadable() {
        this.assertOpen();
        if ( this.mOpenOption == UFileOpenOption.CREATE || this.mOpenOption == UFileOpenOption.CREATE_OVERWRITE ) {
            throw new IllegalStateException( "Native external file channel is not opened for read" );
        }
    }

    protected void assertWritable() {
        this.assertOpen();
        if ( this.mOpenOption != UFileOpenOption.CREATE && this.mOpenOption != UFileOpenOption.CREATE_OVERWRITE ) {
            throw new IllegalStateException( "Native external file channel is not opened for write" );
        }
    }

    protected void assertAppendable() {
        this.assertOpen();
        if ( this.mOpenOption != UFileOpenOption.APPEND ) {
            throw new IllegalStateException( "Native external file channel is not opened for append" );
        }
    }
}
