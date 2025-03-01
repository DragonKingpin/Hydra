package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.walnut.sparta.ucdn.console.ufm.event.UFMEventListener;

import java.io.IOException;
import java.util.Collection;

public interface FileMultiDistributionService extends Pinenut {
    void fileDistribution( FileNode fileNode, String topic ) throws IOException, InterruptedException;

    void test() throws UMBServiceException;

    BroadcastControlConsumer getTransmitConsumer( String topic,String group );

    BroadcastControlProducer getTransmitProducer();

    FileMultiDistributionService registerFileTransmitCompleteEventListener( UFMEventListener listener ) ;

    FileMultiDistributionService deregisterFileTransmitCompleteEventListener( UFMEventListener listener ) ;

    Collection<UFMEventListener> fetchFileTransmitCompleteEventListeners();

    boolean hasStarted();

    void start() throws UMBServiceException ;

    void shutdown();

    UFMConfig getConfig();
}
