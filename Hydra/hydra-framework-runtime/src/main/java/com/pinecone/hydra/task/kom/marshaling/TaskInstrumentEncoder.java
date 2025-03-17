package com.pinecone.hydra.task.kom.marshaling;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.task.kom.entity.ElementNode;

public interface TaskInstrumentEncoder extends Pinenut {
    Object encode(ElementNode node);
}
