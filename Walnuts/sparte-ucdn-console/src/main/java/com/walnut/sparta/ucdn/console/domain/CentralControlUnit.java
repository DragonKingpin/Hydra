package com.walnut.sparta.ucdn.console.domain;

import com.pinecone.framework.util.id.GUID;

public interface CentralControlUnit {
    void register( GUID guid, Object object );

    Object getLock( GUID guid );
}
