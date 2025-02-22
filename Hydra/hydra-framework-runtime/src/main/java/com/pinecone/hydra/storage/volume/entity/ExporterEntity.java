package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.StorageExportIORequest;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;

import java.io.IOException;

public interface ExporterEntity extends Pinenut {
    VolumeManager getVolumeManager();
    void setVolumeManager(VolumeManager volumeManager);

   StorageExportIORequest getStorageIORequest();
   void setStorageIORequest(StorageExportIORequest storageExportIORequest);

    StorageIOResponse export() throws IOException;
    StorageIOResponse export( Number offset, Number endSize ) throws IOException;
    StorageIOResponse export(CacheBlock cacheBlock, Number offset, Number endSize, byte[] buffer) throws UIOException;

    Chanface getChannel();
    void setChannel( Chanface channel );
}
