package com.pinecone.hydra.storage.file.fat.io;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.source.FileChunkLocationManipulator;

import java.util.HashMap;
import java.util.Map;

public class LinearVolumeSpaceAllocator implements VolumeSpaceAllocator {
    protected final FileChunkLocationManipulator mLocationManipulator;
    protected final Map<GUID, Long>              mReservedEndOffsets;

    public LinearVolumeSpaceAllocator( FileChunkLocationManipulator locationManipulator ) {
        this.mLocationManipulator = locationManipulator;
        this.mReservedEndOffsets  = new HashMap<>();
    }

    @Override
    public synchronized long allocate( GUID volumeGuid, long lengthBytes ) {
        long startOffset = this.mReservedEndOffsets.computeIfAbsent( volumeGuid, this::loadMaxEndOffset );
        this.mReservedEndOffsets.put( volumeGuid, startOffset + lengthBytes );
        return startOffset;
    }

    protected long loadMaxEndOffset( GUID volumeGuid ) {
        Long maxEndOffset = this.mLocationManipulator.getMaxEndOffsetByVolumeGuid( volumeGuid );
        return maxEndOffset == null ? 0L : maxEndOffset;
    }
}
