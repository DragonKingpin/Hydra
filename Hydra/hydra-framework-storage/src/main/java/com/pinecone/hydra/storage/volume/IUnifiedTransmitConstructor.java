package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.volume.entity.ExporterEntity;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;
import com.pinecone.hydra.storage.volume.entity.ReceiveEntity;

public interface IUnifiedTransmitConstructor extends Pinenut {
    ReceiveEntity getReceiveEntity(Class< ? extends LogicVolume> volumeClass, Object... params);

    ExporterEntity getExportEntity( Class< ? extends LogicVolume > volumeClass, Object... params );
}
