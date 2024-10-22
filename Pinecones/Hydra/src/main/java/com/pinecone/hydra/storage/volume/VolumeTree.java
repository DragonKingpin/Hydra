package com.pinecone.hydra.storage.volume;

import com.pinecone.hydra.system.ko.kom.KOMInstrument;

public interface VolumeTree extends KOMInstrument {
    VolumeConfig KernelVolumeConfig = new KernelVolumeConfig();

}
