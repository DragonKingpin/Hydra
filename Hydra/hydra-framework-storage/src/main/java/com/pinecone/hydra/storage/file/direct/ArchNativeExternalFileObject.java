package com.pinecone.hydra.storage.file.direct;

import java.io.File;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ArchElementNode;

public abstract class ArchNativeExternalFileObject extends ArchElementNode implements ExternalFileObject {
    protected File      mNativeFile;

    public ArchNativeExternalFileObject( File file ) {
        this.mNativeFile  = file;
        this.name         = file.getName();
        long lastModified = file.lastModified();
        this.updateTime   = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastModified), ZoneId.systemDefault());
        this.createTime   = this.updateTime;
    }

    @Override
    public KOMFileSystem parentFileSystem() {
        return null;
    }

    public File getNativeFile() {
        return this.mNativeFile;
    }

    @Override
    public URI toURI() {
        return this.mNativeFile.toURI();
    }

    public String getURI() {
        return this.toURI().toString();
    }

    @Override
    public String getPath() {
        return this.mNativeFile.getPath();
    }

    @Override
    public boolean delete() {
        return this.mNativeFile.delete();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

}
