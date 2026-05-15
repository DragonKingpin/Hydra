package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.block.BlockVolume;
import com.pinecone.hydra.storage.volume.block.SimpleVolume;
import com.pinecone.hydra.storage.volume.block.SpannedVolume;
import com.pinecone.hydra.storage.volume.block.StripedVolume;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.io.PhysicalAccessor;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface VolumeManager extends Pinenut {
    // TODO: Wire UOFS journal recovery to a VolumeManager-backed phase so OBJECT cleanup
    // and BLOCK_EXTENT readability checks can use a real FatChunkStore instead of metadata-only recovery.
    VolumeConfig getConfig();

    void registerPhysical( PhysicalAccessor physicalAccessor );

    void persistPhysical( VolumePhysical physical );

    void registerVolume( Volume volume );

    Optional<PhysicalAccessor> findPhysical( GUID guid );

    Optional<Volume> findVolume( GUID guid );

    Volume affirmVolume( GUID guid );

    Volume loadVolume( GUID guid ) throws IOException;

    List<Volume> listVolumes() throws IOException;

    SimpleVolume createSimpleVolume( GUID guid, String name, VolumeExtent backingExtent );

    SpannedVolume createSpannedVolume( GUID guid, String name, Iterable<VolumeExtent> extents );

    StripedVolume createStripedVolume( GUID guid, String name, long stripeUnit, Iterable<VolumeExtent> members );

    default BlockVolume affirmBlockVolume( GUID guid ) {
        Volume volume = this.affirmVolume( guid );
        if ( !( volume instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Volume is not block-addressable: " + guid );
        }
        return (BlockVolume) volume;
    }

    int read( GUID volumeGuid, long position, ByteBuffer dst ) throws IOException;

    int write( GUID volumeGuid, long position, ByteBuffer src ) throws IOException;

    void refreshVolumeUsage( GUID volumeGuid ) throws IOException;

    void flush( GUID volumeGuid ) throws IOException;
}

