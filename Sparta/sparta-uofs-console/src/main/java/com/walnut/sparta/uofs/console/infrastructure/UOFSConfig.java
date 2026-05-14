package com.walnut.sparta.uofs.console.infrastructure;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UOFSConfig extends Pinenut {
    String getPhysicalVolumeType();

    String getSimpleVolumeType();

    String getSpannedVolumeType();

    String getStripedVolumeType();

}
