package com.peanut.titan.system;

import com.peanut.titan.storage.TitanStorageRegiment;
import com.pinecone.framework.system.ModularizedSubsystem;
import com.pinecone.framework.system.SynergicSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;

public interface TitanSubsystem extends SynergicSystem, ModularizedSubsystem, Lord, Slf4jTraceable {

    TitanStorageRegiment storageRegiment();

    VolumeManager volumeManager();

    KOMFileSystem fileSystem();
}
