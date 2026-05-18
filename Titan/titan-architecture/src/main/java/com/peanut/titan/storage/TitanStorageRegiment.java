package com.peanut.titan.storage;

import com.pinecone.framework.system.regime.Regiment;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface TitanStorageRegiment extends Regiment, Slf4jTraceable {

    VolumeManager volumeManager();

    KOMFileSystem fileSystem();

    FatChunkInstrument fatChunkInstrument();

    boolean isVolumeManagerAvailable();

    boolean isFileSystemAvailable();
}
