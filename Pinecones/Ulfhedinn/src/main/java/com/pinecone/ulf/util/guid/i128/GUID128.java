package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public interface GUID128 extends GUID {

    long getMostSignificantBits();

    long getLeastSignificantBits();

    UUID toUUID();

    int version();

    int variant();

    int clockSequence() ;

    long node() ;

    @Override
    default int sizeof() {
        return 16; // 128 bits = 16 bytes
    }

}
