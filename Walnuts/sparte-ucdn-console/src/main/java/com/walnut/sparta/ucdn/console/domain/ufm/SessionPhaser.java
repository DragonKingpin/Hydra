package com.walnut.sparta.ucdn.console.domain.ufm;

import com.pinecone.framework.util.id.GUID;

public interface SessionPhaser {
    void registerDistributionLock(GUID guid, Object object );

    Object getDistributionLock(GUID guid );

    void registerDistributionClusterCount( GUID guid, long count );

    long getDistributionClusterCount( GUID guid );

    void incrementDistributionClusterCount( GUID guid );

    void resetDistributionClusterCount( GUID guid );

    void registerDistributionSynchronize( GUID guid, Long count );

    long getDistributionSynchronize( GUID guid );

    void incrementDistributionSynchronize( GUID guid );

    void resetDistributionSynchronize( GUID guid );

}
