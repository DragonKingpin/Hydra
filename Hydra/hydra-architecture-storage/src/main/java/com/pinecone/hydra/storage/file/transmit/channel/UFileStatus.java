package com.pinecone.hydra.storage.file.transmit.channel;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.external.ExternalFile;

public final class UFileStatus implements Pinenut {
    protected final GUID    mGuid;
    protected final String  mName;
    protected final long    mPhysicalSize;
    protected final long    mLogicSize;
    protected final long    mDefinitionSize;
    protected final boolean mUploadSuccessful;

    public UFileStatus( FileNode fileNode ) {
        this.mGuid             = fileNode.getGuid();
        this.mName             = fileNode.getName();
        this.mPhysicalSize     = fileNode.getPhysicalSize();
        this.mLogicSize        = fileNode.getLogicSize();
        this.mDefinitionSize   = fileNode.getDefinitionSize();
        this.mUploadSuccessful = fileNode.isUploadSuccess();
    }

    public UFileStatus( ExternalFile externalFile ) {
        long size = externalFile.size().longValue();
        this.mGuid             = externalFile.getGuid();
        this.mName             = externalFile.getName();
        this.mPhysicalSize     = size;
        this.mLogicSize        = size;
        this.mDefinitionSize   = size;
        this.mUploadSuccessful = externalFile.exists();
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public String getName() {
        return this.mName;
    }

    public long getPhysicalSize() {
        return this.mPhysicalSize;
    }

    public long getLogicSize() {
        return this.mLogicSize;
    }

    public long getDefinitionSize() {
        return this.mDefinitionSize;
    }

    public boolean isUploadSuccessful() {
        return this.mUploadSuccessful;
    }
}
