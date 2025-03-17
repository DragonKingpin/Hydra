package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.framework.system.prototype.Pinenut;

public class UFMDClusterFrame implements Pinenut {
    private byte[] bytes;

    private String path;

    private long segId;

    private long totalSegNum;

    private long offset;

    public UFMDClusterFrame( byte[] bytes, String path, long segId, long totalSegNum, long offset ) {
        this.bytes = bytes;
        this.path = path;
        this.segId = segId;
        this.totalSegNum = totalSegNum;
        this.offset = offset;
    }

    public UFMDClusterFrame(){}

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSegId() {
        return segId;
    }

    public void setSegId(long segId) {
        this.segId = segId;
    }

    public long getTotalSegNum(){
        return this.totalSegNum;
    }

    public void setTotalSegNum( long totalSegNum ){
        this.totalSegNum = totalSegNum;
    }

    public long getOffset(){
        return this.offset;
    }

    public void setOffset( long offset ){
        this.offset = offset;
    }
}
