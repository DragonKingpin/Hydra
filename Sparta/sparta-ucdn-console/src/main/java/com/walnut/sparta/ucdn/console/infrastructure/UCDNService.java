package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.version.TitanVersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.walnut.redstone.messge.PrimaryMessageWareStone;
import com.walnut.sparta.ucdn.console.ufm.UFMConfig;

public interface UCDNService extends Slf4jTraceable {
    KOMFileSystem getKOMFileSystem();

    UniformVolumeManager getUniformVolumeManager();

    TitanBucketInstrument getTitanBucketInstrument();

    TitanVersionManage getTitanVersionManage();

    ServicesInstrument getServicesInstrument();

    // TODO, For next, that will to systemically integrate the Primary-Middleware-Stone into the uniform-director.
    PrimaryMessageWareStone getPrimaryMessageMiddlewareDirector();

    UniformServiceManager getUniformServiceManager();

    UFMConfig getClusterFileSynchronizationConfig();
}
