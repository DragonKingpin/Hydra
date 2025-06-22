package com.pinecone.ulf.util.guid.i64;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.GuidGenerateException;
import com.pinecone.ulf.util.guid.i64.worker.WorkerIdAssigner;

public interface GuidAllocator64 extends GuidAllocator {

    long nextGUIDi64() throws GuidGenerateException;

    String explain( long guid64 );

    GUID nextGUID64();

    void setWorkerIdAssigner( WorkerIdAssigner workerIdAssigner );

}
