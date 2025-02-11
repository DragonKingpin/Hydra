package com.walnut.sparta.ucdn.console.domain.engine;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;

import java.io.IOException;

public interface FileDistributionEngine extends Pinenut {
    void fileDistribution(FileNode fileNode, String topic) throws IOException, InterruptedException;

    void test() throws UMBServiceException;

    BroadcastControlConsumer getConsumer( String topic,String group );

    BroadcastControlProducer getProducer();
}
