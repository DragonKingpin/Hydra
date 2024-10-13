package com.pinecone.hydra.file;

public class KOFSFrameSegmentNaming implements FrameSegmentNaming {
    @Override
    public String naming( String fileName,long segId,String crc3 ){
        return String.format( "%s_seg%d_%s.frame", fileName, segId, crc3 );
    }
}
