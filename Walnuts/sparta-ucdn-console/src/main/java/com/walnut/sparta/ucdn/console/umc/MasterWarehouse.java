package com.walnut.sparta.ucdn.console.umc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sparta.ucdn.console.umc.ufm.SessionPhaser;
import com.walnut.sparta.ucdn.console.umc.ufmc.ExternalSessionPhaser;

public interface MasterWarehouse extends Pinenut {
    SessionPhaser getSessionPhaser();

    KOMFileSystem getKOMFileSystem();

    UniformVolumeManager getUniformVolumeManager();

    UlfBroadcastControlNode getKafkaClient();

    UlfBroadcastControlNode getKafkaEFileClient();

    UlfBroadcastControlNode getRocketClient();

    UlfBroadcastControlNode getRocketEFileClient();

    ExternalSessionPhaser   getExternalSessionPhaser();
}
