package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;

public class ArchCluster implements Cluster {
    private long enumId;
    private GUID fileGuid;
    private GUID segGuid;
    private long segId;
    private long crc32;
    private long size;

    public ArchCluster() {
    }

    public ArchCluster(long enumId, GUID fileGuid, GUID segGuid, long segId, long crc32, long size) {
        this.enumId = enumId;
        this.fileGuid = fileGuid;
        this.segGuid = segGuid;
        this.segId = segId;
        this.crc32 = crc32;
        this.size = size;
    }

    @Override
    public long getEnumId() {
        return enumId;
    }


    @Override
    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    @Override
    public GUID getFileGuid() {
        return fileGuid;
    }


    @Override
    public void setFileGuid(GUID fileGuid) {
        this.fileGuid = fileGuid;
    }

    @Override
    public GUID getSegGuid() {
        return this.segGuid;
    }

    @Override
    public void setSegGuid(GUID segGuid) {
        this.segGuid = segGuid;
    }


    @Override
    public long getSegId() {
        return segId;
    }


    @Override
    public void setSegId(long segId) {
        this.segId = segId;
    }


    @Override
    public long getCrc32() {
        return this.crc32;
    }


    @Override
    public void setCrc32( long crc32 ) {
        this.crc32 = crc32;
    }


    @Override
    public long getSize() {
        return size;
    }


    @Override
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public void remove() {

    }

    @Override
    public void save() {

    }

    public String toString() {
        return "ArchCluster{enumId = " + enumId + ", fileGuid = " + fileGuid + ", segGuid = " + segGuid + ", segId = " + segId + ", crc32 = " + crc32 + ", size = " + size + "}";
    }
}
