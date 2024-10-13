package com.pinecone.hydra.file;

public class KOFSFrameSegmentNaming {
    public static String frameName( String fileName,long segId,String crc3 ){
        return String.format("%s_seg%d_%s.frame", fileName, segId, crc3);
    }
}
