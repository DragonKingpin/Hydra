package com.walnut.sparta.ucdn.console.umc.ufm;

public class UFMDClusterFrame {
    private byte[] bytes;

    private String path;

    private long segId;

    public UFMDClusterFrame( byte[] bytes, String path, long segId ) {
        this.bytes = bytes;
        this.path = path;
        this.segId = segId;
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
}
