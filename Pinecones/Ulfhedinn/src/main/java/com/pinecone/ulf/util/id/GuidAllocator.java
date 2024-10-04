package com.pinecone.ulf.util.id;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.id.exception.GuidGenerateException;
import com.pinecone.ulf.util.id.worker.WorkerIdAssigner;

public interface GuidAllocator extends Pinenut {

    /**
     * Get a unique ID
     *
     * @return UID
     * @throws GuidGenerateException
     */
    long getGUID64() throws GuidGenerateException;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param guid64
     * @return Parsed info
     */
    String parseGUID64( long guid64 );

    //   GUID64 parseGUID64(long uid);

    GUID nextGUID64();

    GUID nextGUID72();

    void setWorkerIdAssigner( WorkerIdAssigner workerIdAssigner );
}
