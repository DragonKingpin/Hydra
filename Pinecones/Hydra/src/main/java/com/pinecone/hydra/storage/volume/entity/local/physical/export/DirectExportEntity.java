package com.pinecone.hydra.storage.volume.entity.local.physical.export;

import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.entity.ExporterEntity;

import java.io.IOException;
import java.sql.SQLException;

public interface DirectExportEntity extends ExporterEntity {
    StorageIOResponse export(Number offset, Number endSize) throws IOException;
}
