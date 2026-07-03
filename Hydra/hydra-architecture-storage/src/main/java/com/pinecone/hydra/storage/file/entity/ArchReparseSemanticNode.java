package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.storage.file.KOMFileSystem;

import java.time.LocalDateTime;

public abstract class ArchReparseSemanticNode extends ArchElementNode implements ReparseSemanticNode {
    protected String                  reparsedPoint;
    protected String                  targetScheme;
    protected String                  extConfig;

    protected KOMFileSystem           fileSystem;

    public ArchReparseSemanticNode() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public ArchReparseSemanticNode(KOMFileSystem fileSystem ) {
        this();
        this.fileSystem = fileSystem;
        GuidAllocator guidAllocator = this.fileSystem.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID() );
    }

    @Override
    public String getReparsedPoint() {
        return this.reparsedPoint;
    }

    @Override
    public void setReparsedPoint(String reparsedPoint) {
        this.reparsedPoint = reparsedPoint;
    }

    @Override
    public String getTargetScheme() {
        return this.targetScheme;
    }

    @Override
    public void setTargetScheme( String targetScheme ) {
        this.targetScheme = targetScheme;
    }

    @Override
    public String getExtConfig() {
        return this.extConfig;
    }

    @Override
    public void setExtConfig( String extConfig ) {
        this.extConfig = extConfig;
    }

    @Override
    public KOMFileSystem parentFileSystem() {
        return this.fileSystem;
    }
}
