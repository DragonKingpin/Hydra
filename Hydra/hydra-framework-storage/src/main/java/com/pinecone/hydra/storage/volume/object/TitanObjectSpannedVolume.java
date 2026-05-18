package com.pinecone.hydra.storage.volume.object;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TitanObjectSpannedVolume extends ArchVolume implements ObjectSpannedVolume {
    protected final List<VolumeExtent> mExtents;
    protected int                      mnNextIndex;

    public TitanObjectSpannedVolume() {
        super();
        this.mVolumeType = VolumeType.SPANNED;
        this.mMappingMode = VolumeMappingMode.VOLUME_DIRECT_OBJECT;
        this.mExtents = new ArrayList<>();
    }

    public TitanObjectSpannedVolume( GUID guid, String name ) {
        this();
        this.mGuid = guid;
        this.mszName = name;
        this.mStatus = VolumeStatus.READY;
    }

    @Override
    public List<VolumeExtent> getExtents() {
        return this.mExtents;
    }

    @Override
    public void addExtent( VolumeExtent extent ) {
        if ( extent == null ) {
            throw new IllegalArgumentException( "Object spanned extent is null" );
        }
        if ( !( extent.getChildVolume() instanceof ObjectSimpleVolume ) ) {
            throw new IllegalArgumentException( "Object spanned volume only accepts object simple child volumes" );
        }
        extent.setParentOffset( this.mnLogicalSize );
        this.mExtents.add( extent );
        this.mnLogicalSize += extent.getLength();
    }

    @Override
    public ObjectSimpleVolume selectObjectVolume( String namespace, String objectName, long objectSize ) {
        if ( this.mExtents.isEmpty() ) {
            throw new IllegalStateException( "Object spanned volume has no child volume: " + this.getGuid() );
        }
        VolumeExtent extent = this.mExtents.get( this.mnNextIndex % this.mExtents.size() );
        this.mnNextIndex++;
        return (ObjectSimpleVolume) extent.getChildVolume();
    }

    @Override
    public String allocateObjectKey( String namespace, String objectName, long objectSize ) throws IOException {
        return this.selectObjectVolume( namespace, objectName, objectSize )
                .allocateObjectKey( namespace, objectName, objectSize );
    }

    @Override
    public Path resolveObjectPath( String objectKey ) throws IOException {
        if ( this.mExtents.isEmpty() ) {
            throw new IllegalStateException( "Object spanned volume has no child volume: " + this.getGuid() );
        }
        return ( (ObjectSimpleVolume) this.mExtents.get( 0 ).getChildVolume() ).resolveObjectPath( objectKey );
    }

    @Override
    public void deleteObject( String objectKey ) throws IOException {
        for ( VolumeExtent extent : this.mExtents ) {
            ( (ObjectSimpleVolume) extent.getChildVolume() ).deleteObject( objectKey );
        }
    }

    @Override
    public void flush() {
    }
}
