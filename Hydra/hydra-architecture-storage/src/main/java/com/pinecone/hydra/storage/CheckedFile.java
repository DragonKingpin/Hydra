package com.pinecone.hydra.storage;

import com.pinecone.hydra.storage.mfs.UFile;

public interface CheckedFile extends UFile {

    long getChecksum();

    void setChecksum(long checksum);

    int getParityCheck();

    void setParityCheck(int parityCheck);


}
