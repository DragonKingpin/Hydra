package com.pinecone.ulf.util.guid.i64;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.GuidGenerateException;
import com.pinecone.ulf.util.guid.i64.worker.WorkerIdAssigner;

public interface GuidAllocator72 extends GuidAllocator64 {

    GUID nextGUID72();

}
