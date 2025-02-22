package com.pinecone.hydra.storage.file.entity;

import com.pinecone.hydra.storage.file.source.LocalClusterManipulator;

import java.time.LocalDateTime;

public class GenericLocalCluster extends ArchCluster implements LocalCluster {
    private LocalDateTime               createTime;
    private LocalDateTime               updateTime;
    private String                      sourceName;
    private LocalClusterManipulator       localClusterManipulator;
    private long                        definitionSize;
    private long                        fileStartOffset;

    @Override
    public long getDefinitionSize() {
        return this.definitionSize;
    }

    @Override
    public void setDefinitionSize(long definitionSize) {
        this.definitionSize = definitionSize;
    }

    public GenericLocalCluster() {
    }

    public GenericLocalCluster(LocalDateTime createTime, LocalDateTime updateTime, String sourceName) {
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.sourceName = sourceName;
    }

    public GenericLocalCluster(LocalClusterManipulator localClusterManipulator ) {
        this.localClusterManipulator = localClusterManipulator;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public long getFileStartOffset() {
        return this.fileStartOffset;
    }

    @Override
    public void setFileStartOffset(long fileStartOffset) {
        this.fileStartOffset = fileStartOffset;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    @Override
    public void setLocalClusterManipulator(LocalClusterManipulator localClusterManipulator) {
        this.localClusterManipulator = localClusterManipulator;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


    public String getSourceName() {
        return sourceName;
    }


    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public void save() {
        LocalCluster frame = this.localClusterManipulator.getClusterByFileWithId(this.getFileGuid(), this.getSegId());
        if( frame == null ){
            this.localClusterManipulator.insert(this);
        }else {
            this.localClusterManipulator.update( this );
        }
    }

    @Override
    public void remove() {
        this.localClusterManipulator.remove( this.getSegGuid() );
    }
    public String toString() {
        return "GenericLocalCluster{createTime = " + createTime + ", updateTime = " + updateTime + ", sourceName = " + sourceName + "}";
    }


}
