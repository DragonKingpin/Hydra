package com.pinecone.hydra.storage.file.fat.policy;

public class TieredChunkSizingPolicy implements ChunkSizingPolicy {
    public static final long KB = 1024L;
    public static final long MB = 1024L * KB;
    public static final long GB = 1024L * MB;
    public static final long TB = 1024L * GB;

    @Override
    public long chooseChunkSize( long fileSize ) {
        if ( fileSize <= 512L * KB ) {
            return Math.max( fileSize, 1L );
        }
        if ( fileSize <= 128L * MB ) {
            return 8L * MB;
        }
        if ( fileSize <= 16L * GB ) {
            return 64L * MB;
        }
        if ( fileSize <= TB ) {
            return 256L * MB;
        }
        return 512L * MB;
    }
}
