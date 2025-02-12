package com.walnut.sparta.ucdn.console.domain.ufm;

import com.pinecone.framework.util.id.GUID;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class UFMSessionPhaser implements SessionPhaser {
    // File.Guid => Lock
    private ConcurrentMap<GUID, Object> fileLocksMap;

    // File.Guid => Cluster.count (N)
    private ConcurrentMap<GUID, Long>   clusterComplatedPhaserMap;

    // File.Guid => Consumer.count (N)
    private ConcurrentMap<GUID, Long>   consumerComplatedPhaserMap;

    public UFMSessionPhaser() {
        this.fileLocksMap          = new ConcurrentHashMap<>();
        this.clusterComplatedPhaserMap    = new ConcurrentHashMap<>();
        this.consumerComplatedPhaserMap   = new ConcurrentHashMap<>();
    }


    @Override
    public void registerDistributionLock( GUID guid, Object object ) {
        this.fileLocksMap.put( guid, object );
    }

    @Override
    public Object getDistributionLock( GUID guid ) {
        return this.fileLocksMap.get( guid );
    }

    @Override
    public void registerDistributionClusterCount( GUID guid, long count ) {
        this.clusterComplatedPhaserMap.put( guid,count );
    }

    @Override
    public long getDistributionClusterCount(GUID guid) {
        return this.clusterComplatedPhaserMap.get( guid );
    }

    @Override
    public void incrementDistributionClusterCount(GUID guid) {
        Long l = this.clusterComplatedPhaserMap.get(guid);
        this.clusterComplatedPhaserMap.put( guid, l+1 );
    }

    @Override
    public void resetDistributionClusterCount(GUID guid) {
        this.clusterComplatedPhaserMap.put( guid, 0L );
    }

    @Override
    public void registerDistributionSynchronize(GUID guid, Long count) {
        this.consumerComplatedPhaserMap.put( guid, count );
    }

    @Override
    public long getDistributionSynchronize(GUID guid) {
        return this.consumerComplatedPhaserMap.get( guid );
    }

    @Override
    public void incrementDistributionSynchronize(GUID guid) {
        Long l = this.consumerComplatedPhaserMap.get(guid);
        this.consumerComplatedPhaserMap.put( guid, l+1 );
    }

    @Override
    public void resetDistributionSynchronize(GUID guid) {
        this.consumerComplatedPhaserMap.put( guid, 0L );
    }
}
