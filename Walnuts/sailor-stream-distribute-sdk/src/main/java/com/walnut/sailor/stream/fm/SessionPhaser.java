package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.sailor.stream.fm.session.UFMCTransaction;

import java.io.RandomAccessFile;

public interface SessionPhaser extends Pinenut {

    void registerSessionTransaction( Long sessionId, UFMCTransaction ufmcTransaction);

    UFMCTransaction getUFMCTransaction( Long sessionId );

    void removeUFMCTransaction( Long sessionId );

    void registerFileOutputStream( Long sessionId, RandomAccessFile randomAccessFile );

    RandomAccessFile getFileOutputStream( Long sessionId );

    void removeFileOutputStream( Long sessionId );

}
