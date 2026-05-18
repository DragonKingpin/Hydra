package com.pinecone.hydra.storage.volume.io;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalDirectoryPhysicalAccessor extends LocalFilePhysicalAccessor {
    public LocalDirectoryPhysicalAccessor( GUID guid, String name, Path rootPath, long capacity ) throws IOException {
        this( guid, name, rootPath, capacity, new KernelVolumeConfig() );
    }

    public LocalDirectoryPhysicalAccessor(
            GUID guid,
            String name,
            Path rootPath,
            long capacity,
            VolumeConfig config
    ) throws IOException {
        super(
                guid,
                name,
                rootPath.resolve( config.getVolumeDataDirectory() ).resolve( config.getBlockBackingFileName() ),
                capacity
        );
        Files.createDirectories( rootPath.resolve( config.getTitanHomeDirectory() ) );
        Files.createDirectories( rootPath.resolve( config.getVolumeDataDirectory() ) );
    }

    public LocalDirectoryPhysicalAccessor(
            GUID guid,
            String name,
            Path rootPath,
            long capacity,
            VolumeAllocationMode allocationMode,
            long allocationUnit
    ) throws IOException {
        this( guid, name, rootPath, capacity, allocationMode, allocationUnit, new KernelVolumeConfig() );
    }

    public LocalDirectoryPhysicalAccessor(
            GUID guid,
            String name,
            Path rootPath,
            long capacity,
            VolumeAllocationMode allocationMode,
            long allocationUnit,
            VolumeConfig config
    ) throws IOException {
        super(
                guid,
                name,
                rootPath.resolve( config.getVolumeDataDirectory() ).resolve( config.getBlockBackingFileName() ),
                capacity,
                allocationMode,
                allocationUnit
        );
        Files.createDirectories( rootPath.resolve( config.getTitanHomeDirectory() ) );
        Files.createDirectories( rootPath.resolve( config.getVolumeDataDirectory() ) );
    }

    @Override
    public VolumePhysicalType getPhysicalType() {
        return VolumePhysicalType.LOCAL_DIR;
    }
}

