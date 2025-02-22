package com.pinecone.hydra.storage.volume.entity.local.striped.export.channel;

import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.StorageExportIORequest;
import com.pinecone.hydra.storage.volume.entity.Exporter;
import com.pinecone.hydra.storage.volume.entity.StripedVolume;

public interface StripedChannelExport extends Exporter {
    StorageIOResponse export() throws UIOException;

    StorageIOResponse export( Number offset, Number endSize ) throws UIOException;

    VolumeManager getVolumeManager();

    StorageExportIORequest getStorageIORequest();

    Chanface getFileChannel();

    StripedVolume getStripedVolume();
}
