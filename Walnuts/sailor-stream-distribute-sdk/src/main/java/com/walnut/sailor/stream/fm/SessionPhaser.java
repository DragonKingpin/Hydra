package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.sailor.stream.fm.session.SFMTransaction;

import java.io.RandomAccessFile;

public interface SessionPhaser extends Pinenut {

    void registerSessionTransaction( Long sessionId, SFMTransaction SFMTransaction);

    SFMTransaction getSFMTransaction( Long sessionId );

    void removeSFMTransaction( Long sessionId );

    void registerFileOutputStream( Long sessionId, RandomAccessFile randomAccessFile );

    RandomAccessFile getFileOutputStream( Long sessionId );

    void removeFileOutputStream( Long sessionId );

}
