package com.pinecone.hydra.storage.volume.entity.local.spanned.export;

import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.StorageExportIORequest;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.ArchExportEntity;
import com.pinecone.hydra.storage.volume.entity.SpannedVolume;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;

public class TitanSpannedExportEntity64 extends ArchExportEntity implements SpannedExportEntity64{
    protected SpannedVolume    spannedVolume;

    protected SpannedExport64  spannedExport;

    public TitanSpannedExportEntity64(VolumeManager volumeManager, StorageExportIORequest storageExportIORequest, Chanface channel, SpannedVolume spannedVolume) {
        super(volumeManager, storageExportIORequest, channel);
        this.spannedVolume = spannedVolume;
        this.spannedExport = new TitanSpannedExport64( this );
    }

    @Override
    public StorageIOResponse export() throws UIOException {
        return this.spannedExport.export(this.channel);
    }

    @Override
    public StorageIOResponse export(Number offset, Number endSize) throws UIOException {
        return null;
    }

    @Override
    public StorageIOResponse export(CacheBlock cacheBlock, Number offset, Number endSize, byte[] buffer) {
        return null;
    }

    @Override
    public SpannedVolume getSpannedVolume() {
        return this.spannedVolume;
    }
}
