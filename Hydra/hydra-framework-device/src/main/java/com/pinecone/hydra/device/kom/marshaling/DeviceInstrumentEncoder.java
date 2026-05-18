package com.pinecone.hydra.device.kom.marshaling;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.kom.entity.ElementNode;

public interface DeviceInstrumentEncoder extends Pinenut {
    Object encode(ElementNode node);
}
