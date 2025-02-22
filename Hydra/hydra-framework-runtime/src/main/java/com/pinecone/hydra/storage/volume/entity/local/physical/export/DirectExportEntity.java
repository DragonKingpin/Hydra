package com.pinecone.hydra.storage.volume.entity.local.physical.export;

import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.volume.entity.ExporterEntity;

import java.io.IOException;

public interface DirectExportEntity extends ExporterEntity {
    StorageIOResponse export(Number offset, Number endSize) throws IOException;
}
