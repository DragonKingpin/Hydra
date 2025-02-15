package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.id.GUID;

public interface SessionPhaser {
    void registerFileLock(GUID guid, Object object );

    Object getFileLock(GUID guid );

    void registerClusterCount(GUID guid, long count );

    long getClusterCount(GUID guid );

    void incrementClusterCount(GUID guid );

    void resetClusterCount(GUID guid );

    void registerConsumerCount(GUID guid, Long count );

    long getConsumerCount(GUID guid );

    void incrementConsumerCount(GUID guid );

    void resetConsumerCount(GUID guid );

}
