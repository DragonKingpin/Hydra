package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.sailor.stream.fm.session.SFMTransaction;

import java.io.RandomAccessFile;

public interface SessionPhaser extends Pinenut {

    void registerSessionTransaction( Long sessionId, SFMTransaction SFMTransaction );

    SFMTransaction getSFMTransaction( Long sessionId );

    void registerDestinationDirectory( Long sessionId, String destinationDirectory );

    String getDestinationDirectory( Long sessionId );

    void removeSession( Long sessionId );

    void registerFileHandler( Long sessionId, RandomAccessFile randomAccessFile );

    RandomAccessFile getFileHandler( Long sessionId );

}
