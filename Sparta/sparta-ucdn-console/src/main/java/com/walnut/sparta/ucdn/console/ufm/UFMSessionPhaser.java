package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.framework.util.id.GUID;
import com.walnut.sparta.ucdn.console.infrastructure.ClusterLock;
import com.walnut.sparta.ucdn.console.ufm.session.UFMTransaction;

import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//@Component
public class UFMSessionPhaser implements SessionPhaser {
    private ConcurrentMap<Long, UFMTransaction >    sessionTransactions;

    // File.Guid => Lock
    private ConcurrentMap<GUID, Object>             fileLocksMap;

    private ConcurrentMap<GUID, ClusterLock>        ClusterLocksMap;

    // File.Guid => Cluster.count (N)
    private ConcurrentMap<GUID, Long>               clusterComplatedPhaserMap;

    // File.Guid => Consumer.count (N)
    private ConcurrentMap<GUID, Long>               consumerComplatedPhaserMap;

    private ConcurrentMap<GUID, RandomAccessFile>   clusterOutputStreamMap;

    public UFMSessionPhaser() {
        this.sessionTransactions          = new ConcurrentHashMap<>();
        this.fileLocksMap                 = new ConcurrentHashMap<>();
        this.clusterComplatedPhaserMap    = new ConcurrentHashMap<>();
        this.consumerComplatedPhaserMap   = new ConcurrentHashMap<>();
        this.clusterOutputStreamMap       = new ConcurrentHashMap<>();
        this.ClusterLocksMap              = new ConcurrentHashMap<>();
    }


    @Override
    public void registerFileLock( GUID guid, Object object ) {
        this.fileLocksMap.put( guid, object );
    }

    @Override
    public Object getFileLock( GUID guid ) {
        return this.fileLocksMap.get( guid );
    }

    @Override
    public void removeFileLock(GUID guid) {
        this.fileLocksMap.remove( guid );
    }

    @Override
    public void registerClusterLock(GUID guid, ClusterLock clusterLock) {
        this.ClusterLocksMap.put( guid, clusterLock );
    }

    @Override
    public ClusterLock getClusterLock(GUID guid) {
        return this.ClusterLocksMap.get( guid );
    }

    @Override
    public void removeClusterLock(GUID guid) {
        this.ClusterLocksMap.remove( guid );
    }

    @Override
    public void removeClusterCount(GUID guid) {
        this.clusterComplatedPhaserMap.remove( guid );
    }

    @Override
    public void removeConsumerCount(GUID guid) {
        this.consumerComplatedPhaserMap.remove( guid );
    }

    @Override
    public void registerClusterCount( GUID guid, long count ) {
        this.clusterComplatedPhaserMap.put( guid,count );
    }

    @Override
    public long getClusterCount( GUID guid ) {
        return this.clusterComplatedPhaserMap.get( guid );
    }

    @Override
    public void incrementClusterCount( GUID guid ) {
        Long l = this.clusterComplatedPhaserMap.get(guid);
        this.clusterComplatedPhaserMap.put( guid, l+1 );
    }

    @Override
    public void resetClusterCount( GUID guid ) {
        this.clusterComplatedPhaserMap.put( guid, 0L );
    }

    @Override
    public void registerConsumerCount( GUID guid, Long count ) {
        this.consumerComplatedPhaserMap.put( guid, count );
    }

    @Override
    public long getConsumerCount( GUID guid ) {
        return this.consumerComplatedPhaserMap.get( guid );
    }

    @Override
    public void incrementConsumerCount( GUID guid ) {
        Long l = this.consumerComplatedPhaserMap.get(guid);
        this.consumerComplatedPhaserMap.put( guid, l+1 );
    }

    @Override
    public void resetConsumerCount( GUID guid ) {
        this.consumerComplatedPhaserMap.put( guid, 0L );
    }

    @Override
    public void registerSessionTransaction(Long sessionId, UFMTransaction ufmTransaction) {
        this.sessionTransactions.put( sessionId, ufmTransaction );
    }

    @Override
    public UFMTransaction getSessionTransaction(Long sessionId) {
        return this.sessionTransactions.get( sessionId );
    }

    @Override
    public void removeSessionTransaction(Long sessionId) {
        this.sessionTransactions.remove( sessionId );
    }

    @Override
    public void registerClusterOutputStream(GUID guid, RandomAccessFile fileOutputStream) {
        this.clusterOutputStreamMap.put( guid, fileOutputStream );
    }

    @Override
    public RandomAccessFile getClusterOutputStream(GUID guid) {
        return this.clusterOutputStreamMap.get( guid );
    }

    @Override
    public void removeClusterOutputStream(GUID guid) {
        this.clusterOutputStreamMap.remove( guid );
    }
}
