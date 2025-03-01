package com.walnut.sailor.stream.fm;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sailor.stream.fm.event.SFMEventSubscriber;

public interface SingleStreamFileMultiDistributionService extends Pinenut {

    void distributeFile( File file, String directionRouteToken ) throws IOException;

    void distributeFile( String szFileName, String originalDirectory, String directionRouteToken ) throws IOException;

    boolean hasStarted();

    void start() throws UMBServiceException;

    void shutdown();

    SFMConfig getConfig();

    UlfBroadcastControlNode getTransmitClient() ;

    BroadcastControlConsumer getTransmitConsumer() ;

    BroadcastControlProducer getTransmitProducer() ;

    String queryDestinedDirectoryByToken( String token );

    void registerDirectionRoute( String token, String directoryPath );

    void deregisterDirectionRoute( String token );

    SingleStreamFileMultiDistributionService registerFileTransmitCompleteEventSubscriber( SFMEventSubscriber subscriber );

    SingleStreamFileMultiDistributionService deregisterFileTransmitCompleteEventSubscriber( SFMEventSubscriber subscriber );

    Collection<SFMEventSubscriber> fetchFileTransmitCompleteEventSubscribers();

}
