package com.walnut.sailor.stream.fm;

import java.io.File;
import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;

public interface SingleStreamFileMultiDistributionService extends Pinenut {

    void distributeFile( File file, String topic, String destinedDirectory ) throws IOException;

    void distributeFile( String szFileName, String originalDirectory, String topic, String destinedDirectory ) throws IOException;

    boolean hasStarted();

    void start() throws UMBServiceException;

    void shutdown();

    SFMConfig getConfig();

    UlfBroadcastControlNode getControlClient() ;

    BroadcastControlConsumer getControlConsumer() ;

    BroadcastControlProducer getControlProducer() ;

}
