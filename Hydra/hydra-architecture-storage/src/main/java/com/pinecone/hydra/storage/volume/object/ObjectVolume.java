package com.pinecone.hydra.storage.volume.object;

import com.pinecone.hydra.storage.volume.core.Volume;

import java.io.IOException;
import java.nio.file.Path;

public interface ObjectVolume extends Volume {
    String allocateObjectKey( String namespace, String objectName, long objectSize ) throws IOException;

    Path resolveObjectPath( String objectKey ) throws IOException;

    void deleteObject( String objectKey ) throws IOException;
}
