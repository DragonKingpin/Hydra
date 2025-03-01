package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.storage.bucket.TitanBucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.version.TitanVersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;

public interface UCDNService extends Slf4jTraceable {
    KOMFileSystem getKOMFileSystem();

    UniformVolumeManager getUniformVolumeManager();

    TitanBucketInstrument getTitanBucketInstrument();

    TitanVersionManage getTitanVersionManage();

    ServicesInstrument getServicesInstrument();

    DuplexAppointClient getWolfClient();

    UlfBroadcastControlNode getKafkaClient();

    UlfBroadcastControlNode getRocketClient();

    WolfMCServer  getWolfMCServer();

    WolvesAppointServer getWolvesAppointServer();

    UniformServiceManager getUniformServiceManager();
}
