package com.walnut.sparta.ucdn.console.domain;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;

public interface FileDistributionEngine extends Pinenut {
    void FileDistribution(FileNode fileNode, String topic);

    BroadcastControlConsumer getConsumer( String topic,String group );

    BroadcastControlProducer getProducer();
}
