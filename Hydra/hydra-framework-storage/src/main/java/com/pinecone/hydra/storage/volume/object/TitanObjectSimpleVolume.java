package com.pinecone.hydra.storage.volume.object;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.block.SimpleVolume;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class TitanObjectSimpleVolume extends ArchVolume implements ObjectSimpleVolume {
    protected VolumeExtent mBackingExtent;
    protected VolumeConfig mConfig;

    public TitanObjectSimpleVolume() {
        super();
        this.mVolumeType = VolumeType.SIMPLE;
        this.mMappingMode = VolumeMappingMode.VOLUME_DIRECT_OBJECT;
        this.mConfig = new KernelVolumeConfig();
    }

    public TitanObjectSimpleVolume( GUID guid, String name, VolumeExtent backingExtent, VolumeConfig config ) {
        this();
        this.mGuid = guid;
        this.mszName = name;
        this.mConfig = config == null ? new KernelVolumeConfig() : config;
        this.setBackingExtent( backingExtent );
        this.mStatus = VolumeStatus.READY;
    }

    @Override
    public VolumeExtent getBackingExtent() {
        return this.mBackingExtent;
    }

    @Override
    public void setBackingExtent( VolumeExtent backingExtent ) {
        if ( backingExtent == null ) {
            throw new IllegalArgumentException( "Object simple volume backing extent is null" );
        }
        if ( backingExtent.getPhysicalAccessor() == null ) {
            throw new IllegalArgumentException( "Object simple volume requires physical backing" );
        }
        this.mBackingExtent = backingExtent;
        this.mnLogicalSize = backingExtent.getLength();
    }

    @Override
    public String allocateObjectKey( String namespace, String objectName, long objectSize ) {
        String safeNamespace = namespace == null || namespace.isBlank() ? "default" : namespace;
        String safeObjectName = objectName == null || objectName.isBlank() ? UUID.randomUUID().toString() : objectName;
        return this.joinObjectKey(
                this.mConfig.getTitanHomeDirectory(),
                this.mConfig.getObjectDataDirectory(),
                safeNamespace,
                safeObjectName
        );
    }

    @Override
    public Path resolveObjectPath( String objectKey ) throws IOException {
        if ( this.getObjectRoot() == null || this.getObjectRoot().isBlank() ) {
            throw new IOException( "Object simple volume has no object root: " + this.getGuid() );
        }
        return Paths.get( this.getObjectRoot() ).resolve( objectKey );
    }

    @Override
    public void deleteObject( String objectKey ) throws IOException {
        Files.deleteIfExists( this.resolveObjectPath( objectKey ) );
    }

    @Override
    public void flush() {
    }

    protected String joinObjectKey( String ...parts ) {
        String separator = this.mConfig.getPathNameSeparator();
        StringBuilder builder = new StringBuilder();
        for ( String part : parts ) {
            if ( part == null || part.isBlank() ) {
                continue;
            }
            if ( builder.length() > 0 ) {
                builder.append( separator );
            }
            builder.append( this.trimObjectKeyPart( part, separator ) );
        }
        return builder.toString();
    }

    protected String trimObjectKeyPart( String part, String separator ) {
        String ret = part;
        while ( ret.startsWith( separator ) ) {
            ret = ret.substring( separator.length() );
        }
        while ( ret.endsWith( separator ) ) {
            ret = ret.substring( 0, ret.length() - separator.length() );
        }
        return ret;
    }
}
