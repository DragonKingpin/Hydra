package com.pinecone.hydra.storage.volume.io;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalObjectDirectoryPhysicalAccessor implements PhysicalAccessor {
    protected final GUID         mGuid;
    protected final String       mName;
    protected final Path         mRootPath;
    protected final long         mCapacity;
    protected final VolumeConfig mConfig;

    public LocalObjectDirectoryPhysicalAccessor(
            GUID guid,
            String name,
            Path rootPath,
            long capacity,
            VolumeConfig config
    ) throws IOException {
        this.mGuid     = guid;
        this.mName     = name;
        this.mRootPath = rootPath;
        this.mCapacity = capacity;
        this.mConfig   = config;
        Files.createDirectories( this.mRootPath.resolve( this.mConfig.getTitanHomeDirectory() ) );
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    public Path getRootPath() {
        return this.mRootPath;
    }

    @Override
    public VolumePhysicalType getPhysicalType() {
        return VolumePhysicalType.LOCAL_DIR;
    }

    @Override
    public long getCapacity() {
        return this.mCapacity;
    }

    @Override
    public long getCommittedBytes() throws IOException {
        if ( !Files.exists( this.mRootPath ) ) {
            return 0L;
        }
        try ( java.util.stream.Stream<Path> stream = Files.walk( this.mRootPath ) ) {
            return stream
                    .filter( Files::isRegularFile )
                    .mapToLong( this::sizeOf )
                    .sum();
        }
    }

    protected long sizeOf( Path path ) {
        try {
            return Files.size( path );
        }
        catch ( IOException ignored ) {
            return 0L;
        }
    }

    @Override
    public int read( long position, ByteBuffer dst ) {
        throw new UnsupportedOperationException( "Object directory physical accessor does not support block read" );
    }

    @Override
    public int write( long position, ByteBuffer src ) {
        throw new UnsupportedOperationException( "Object directory physical accessor does not support block write" );
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}

