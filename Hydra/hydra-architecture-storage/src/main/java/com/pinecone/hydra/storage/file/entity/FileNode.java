package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.CheckedFile;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;

import java.util.List;

public interface FileNode extends ElementNode, CheckedFile {
    long getChecksum();
    void setChecksum(long checksum);

    int getParityCheck();
    void setParityCheck(int parityCheck);


    void copyValueTo(GUID destinationGuid );
    void copyTo    (GUID destinationGuid);

    GUID getDataAffinityGuid();

    boolean getIsUploadSuccessful();
    void setIsUploadSuccessful( boolean isUploadSuccessful );

    List<FileChunk> getChunks();

    List<FileChunkLocation> getChunkLocations( FileChunk chunk );

    void removeChunks();

    @Override
    default FileNode evinceFileNode() {
        return this;
    }

    long getPhysicalSize();
    void setPhysicalSize(long physicalSize);

    long getLogicSize();
    void setLogicSize(long logicSize);

    long getDefinitionSize();
    void setDefinitionSize(long definitionSize);

    long getCrc32Xor();

    void setCrc32Xor( long crc32Xor );

    boolean getIntegrityCheckEnable();
    void setIntegrityCheckEnable(boolean integrityCheckEnable);

    boolean getDisableChunk();

    void setDisableChunk(boolean disableChunk);

    boolean isUploadSuccess();

    String getPath();

    void setPath( String path );
}
