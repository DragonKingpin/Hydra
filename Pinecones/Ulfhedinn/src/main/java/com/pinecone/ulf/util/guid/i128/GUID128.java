package com.pinecone.ulf.util.guid.i128;

import com.pinecone.framework.util.id.GUID;

import java.util.UUID;

public interface GUID128 extends GUID {

    long getMsb();

    long getLsb();

    UUID toUUID();

}
