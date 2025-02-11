package com.walnut.sparta.ucdn.console.infrastructure.entity;

public class UFMDClusterDO {
    private String filePath;

    private long segId;

    private long size;

    private String crc32;

    private String sourceName;

    public UFMDClusterDO(String filePath, long segId, long size, String crc32, String sourceName) {
        this.filePath = filePath;
        this.segId = segId;
        this.size = size;
        this.crc32 = crc32;
        this.sourceName = sourceName;
    }

    public UFMDClusterDO(){}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSegId() {
        return segId;
    }

    public void setSegId(long segId) {
        this.segId = segId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCrc32() {
        return crc32;
    }

    public void setCrc32(String crc32) {
        this.crc32 = crc32;
    }

    @Override
    public String toString() {
        return "FrameVO{" +
                "filePath='" + filePath + '\'' +
                ", segId=" + segId +
                ", size=" + size +
                ", crc32='" + crc32 + '\'' +
                ", sourceName='" + sourceName + '\'' +
                '}';
    }
}
