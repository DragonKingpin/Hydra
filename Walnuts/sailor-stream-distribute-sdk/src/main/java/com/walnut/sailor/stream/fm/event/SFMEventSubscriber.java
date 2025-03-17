package com.walnut.sailor.stream.fm.event;

import com.pinecone.framework.system.prototype.Pinenut;

public interface SFMEventSubscriber extends Pinenut {
    void afterEventTriggered( String path, String fileName, String directoryPath ) ;
}
