package com.pinecone.hydra.deploy.kom.marshaling;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;

public interface DeployInstrumentEncoder extends Pinenut {
    Object encode(ElementNode node);
}
