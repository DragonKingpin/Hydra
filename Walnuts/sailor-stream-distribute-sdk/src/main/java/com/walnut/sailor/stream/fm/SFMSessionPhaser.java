package com.walnut.sailor.stream.fm;

import com.walnut.sailor.stream.fm.session.SFMTransaction;

import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SFMSessionPhaser implements SessionPhaser {
    private ConcurrentMap<Long, SFMTransaction>    sessionTransactions;

    private ConcurrentMap<Long, RandomAccessFile>  fileOutputStreamMap;

    public SFMSessionPhaser() {
        this.sessionTransactions = new ConcurrentHashMap<>();
        this.fileOutputStreamMap = new ConcurrentHashMap<>();
    }

    @Override
    public void registerSessionTransaction( Long sessionId, SFMTransaction SFMTransaction) {
        this.sessionTransactions.put( sessionId, SFMTransaction);
    }

    @Override
    public SFMTransaction getSFMTransaction( Long sessionId ) {
        return this.sessionTransactions.get( sessionId );
    }

    @Override
    public void removeSFMTransaction( Long sessionId ) {
        this.sessionTransactions.remove( sessionId );
    }

    @Override
    public void registerFileOutputStream( Long sessionId, RandomAccessFile randomAccessFile ) {
        this.fileOutputStreamMap.put( sessionId, randomAccessFile );
    }

    @Override
    public RandomAccessFile getFileOutputStream( Long sessionId ) {
        return this.fileOutputStreamMap.get( sessionId );
    }

    @Override
    public void removeFileOutputStream( Long sessionId ) {
        this.fileOutputStreamMap.remove( sessionId );
    }
}
