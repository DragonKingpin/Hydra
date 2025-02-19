package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.ucdn.console.infrastructure.ClusterLock;
import com.walnut.sparta.ucdn.console.umc.ufm.session.UFMTransaction;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public interface SessionPhaser {
    void registerFileLock(GUID guid, Object object );

    Object getFileLock(GUID guid );

    void removeFileLock( GUID guid );

    void registerClusterLock(GUID guid, ClusterLock clusterLock);

    ClusterLock getClusterLock( GUID guid );

    void removeClusterLock( GUID guid );

    void registerClusterCount(GUID guid, long count );

    long getClusterCount(GUID guid );

    void removeClusterCount( GUID guid );

    void incrementClusterCount(GUID guid );

    void resetClusterCount(GUID guid );

    void registerConsumerCount(GUID guid, Long count );

    long getConsumerCount(GUID guid );

    void removeConsumerCount( GUID guid );

    void incrementConsumerCount(GUID guid );

    void resetConsumerCount(GUID guid );

    void registerSessionTransaction(Long sessionId, UFMTransaction ufmTransaction);

    UFMTransaction getSessionTransaction( Long sessionId );

    void removeSessionTransaction( Long sessionId );

    void registerClusterOutputStream(GUID guid, RandomAccessFile fileOutputStream);

    RandomAccessFile getClusterOutputStream(GUID guid );

    void removeClusterOutputStream( GUID guid );
}
