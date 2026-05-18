package com.pinecone.hydra.storage.volume.object;

import com.pinecone.hydra.storage.volume.block.SpannedVolume;

public interface ObjectSpannedVolume extends SpannedVolume, ObjectVolume {
    ObjectSimpleVolume selectObjectVolume( String namespace, String objectName, long objectSize );
}
