package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.hydra.util.UniformHashing;

public class UofsTransferPathHasher {
    public String hash( String path ) {
        return UniformHashing.sha256Hex( path );
    }
}
