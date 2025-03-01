package com.walnut.sailor.stream.fm;

import com.walnut.sailor.stream.fm.session.SFMTransaction;

import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SFMSessionPhaser implements SessionPhaser {
    private ConcurrentMap<Long, PhaseHandler>    sessionHandlers;

    public SFMSessionPhaser() {
        this.sessionHandlers = new ConcurrentHashMap<>();
    }

    @Override
    public void registerSessionTransaction( Long sessionId, SFMTransaction transaction ) {
        PhaseHandler handler = this.sessionHandlers.computeIfAbsent( sessionId, (k)->{
            return new PhaseHandler();
        } );
        handler.sfmTransaction = transaction;
    }

    @Override
    public void registerDestinationDirectory( Long sessionId, String destinationDirectory ) {
        PhaseHandler handler = this.sessionHandlers.computeIfAbsent( sessionId, (k)->{
            return new PhaseHandler();
        } );
        handler.destinationDirectory = destinationDirectory;
    }

    @Override
    public SFMTransaction getSFMTransaction( Long sessionId ) {
        PhaseHandler handler = this.sessionHandlers.get( sessionId );
        if ( handler != null ) {
            return handler.sfmTransaction;
        }
        return null;
    }

    @Override
    public String getDestinationDirectory( Long sessionId ) {
        PhaseHandler handler = this.sessionHandlers.get( sessionId );
        if ( handler != null ) {
            return handler.destinationDirectory;
        }
        return null;
    }

    @Override
    public void removeSession( Long sessionId ) {
        this.sessionHandlers.remove( sessionId );
    }

    @Override
    public void registerFileHandler( Long sessionId, RandomAccessFile randomAccessFile ) {
        PhaseHandler handler = this.sessionHandlers.computeIfAbsent( sessionId, (k)->{
            return new PhaseHandler();
        } );
        handler.fileHandler = randomAccessFile;
    }

    @Override
    public RandomAccessFile getFileHandler( Long sessionId ) {
        PhaseHandler handler = this.sessionHandlers.get( sessionId );
        if ( handler != null ) {
            return handler.fileHandler;
        }
        return null;
    }

    public static class PhaseHandler {
        public SFMTransaction    sfmTransaction;

        public RandomAccessFile  fileHandler;

        public String            destinationDirectory;
    }
}
