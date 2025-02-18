package com.walnut.sparta.ucdn.console.umc.ufmc;

import com.walnut.sparta.ucdn.console.umc.ufmc.session.UFMCTransaction;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UFMCSessionPhaser implements ExternalSessionPhaser{
    private ConcurrentMap<Long, UFMCTransaction>     sessionTransactions;

    private ConcurrentMap<Long, RandomAccessFile>    fileOutputStreamMap;
    public UFMCSessionPhaser() {
        this.sessionTransactions = new ConcurrentHashMap<>();
        this.fileOutputStreamMap = new ConcurrentHashMap<>();
    }

    @Override
    public void registerSessionTransaction(Long sessionId, UFMCTransaction ufmcTransaction) {
        this.sessionTransactions.put( sessionId, ufmcTransaction );
    }

    @Override
    public UFMCTransaction getUFMCTransaction(Long sessionId) {
        return this.sessionTransactions.get( sessionId );
    }

    @Override
    public void removeUFMCTransaction(Long sessionId) {
        this.sessionTransactions.remove( sessionId );
    }

    @Override
    public void registerFileOutputStream(Long sessionId, RandomAccessFile randomAccessFile) {
        this.fileOutputStreamMap.put( sessionId, randomAccessFile );
    }

    @Override
    public RandomAccessFile getFileOutputStream(Long sessionId) {
        return this.fileOutputStreamMap.get( sessionId );
    }

    @Override
    public void removeFileOutputStream(Long sessionId) {
        this.fileOutputStreamMap.remove( sessionId );
    }
}
