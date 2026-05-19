package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.block.BlockSimpleVolume;
import com.pinecone.hydra.storage.volume.block.BlockSpannedVolume;
import com.pinecone.hydra.storage.volume.block.BlockVolume;
import com.pinecone.hydra.storage.volume.block.StripedVolume;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalSupportTrait;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalStatus;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;
import com.pinecone.hydra.storage.volume.core.StorageSupportDescriptor;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.io.PhysicalAccessor;
import com.pinecone.hydra.storage.volume.object.ObjectSimpleVolume;
import com.pinecone.hydra.storage.volume.object.ObjectSpannedVolume;

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

    void persistVolume( Volume volume );

    void registerVolume( Volume volume );

    Optional<PhysicalAccessor> findPhysical( GUID guid );

    PhysicalAccessor loadPhysicalAccessor( GUID physicalGuid ) throws IOException;

    PhysicalAccessor loadPhysicalAccessor(
            GUID physicalGuid,
            VolumeAllocationMode allocationMode,
            long allocationUnit
    ) throws IOException;

    Optional<Volume> findVolume( GUID guid );

    Volume affirmVolume( GUID guid );

    Volume loadVolume( GUID guid ) throws IOException;

    List<Volume> listVolumes() throws IOException;

    long countVolumes( String name, VolumeType volumeType, VolumeMappingMode mappingMode, VolumeStatus status );

    List<VolumeRecord> listVolumeRecordPage(
            String name,
            VolumeType volumeType,
            VolumeMappingMode mappingMode,
            VolumeStatus status,
            int offset,
            int limit
    );

    VolumeRecord affirmVolumeRecord( GUID guid );

    List<VolumeExtent> getExtentsByParentGuid( GUID parentGuid );

    void retireVolume( GUID guid );

    long countPhysicals( String name, VolumePhysicalType physicalType, VolumePhysicalStatus status, GUID deviceGuid );

    List<VolumePhysical> listPhysicalPage(
            String name,
            VolumePhysicalType physicalType,
            VolumePhysicalStatus status,
            GUID deviceGuid,
            int offset,
            int limit
    );

    VolumePhysical affirmPhysicalRecord( GUID guid );

    long countPhysicalReferences( GUID physicalGuid );

    long countVolumeChildReferences( GUID volumeGuid );

    List<StorageSupportDescriptor> listStorageSupportTypes();

    List<VolumePhysicalSupportTrait> listPhysicalSupportTraits();

    VolumePhysicalSupportTrait getPhysicalSupportTraitByPhysicalGuid( GUID physicalGuid );

    void persistPhysicalSupportTrait( VolumePhysicalSupportTrait trait );

    BlockSimpleVolume createBlockSimpleVolume( GUID guid, String name, VolumeExtent backingExtent );

    ObjectSimpleVolume createObjectSimpleVolume( GUID guid, String name, VolumeExtent backingExtent );

    BlockSpannedVolume createBlockSpannedVolume( GUID guid, String name, Iterable<VolumeExtent> extents );

    ObjectSpannedVolume createObjectSpannedVolume( GUID guid, String name, Iterable<VolumeExtent> extents );

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

